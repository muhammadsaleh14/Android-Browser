package com.example.browserapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.example.browserapp.R
import com.example.browserapp.databinding.ActivityLoginBinding
import com.example.browserapp.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestore


class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val passwordEditText: EditText = binding.passwordEditText
        val passwordToggle: ImageButton = binding.passwordToggle

        // Initially, the password is hidden
        passwordEditText.transformationMethod = PasswordTransformationMethod.getInstance()

        passwordToggle.setOnClickListener {
            // Toggle password visibility
            if (passwordEditText.transformationMethod == null) {
                // Password is visible, so hide it
                passwordEditText.transformationMethod = PasswordTransformationMethod.getInstance()
                passwordToggle.setImageResource(R.drawable.eye_slash_svgrepo_com)
            } else {
                // Password is hidden, so show it
                passwordEditText.transformationMethod = null
                passwordToggle.setImageResource(R.drawable.eye_svgrepo_com)
            }

            // Move cursor to the end of the text
            passwordEditText.setSelection(passwordEditText.text.length)
        }

        val signUpButton: Button = binding.btnSignup
        val loginButtion: Button = binding.btnLogin

        loginButtion.setOnClickListener {
            val _email = binding.txtiptEmail.text.toString()
            val _password = binding.passwordEditText.text.toString()

            if (_email=="" || _password=="" ) {
                Toast.makeText(this, "Fill up both email and password", Toast.LENGTH_SHORT).show()
            }
            else{
                loginUser(_email,_password)
            }

        }
        signUpButton.setOnClickListener{
            val intent = Intent (this@LoginActivity , SignupActivity::class.java)
            startActivity(intent)

        }

    }

    fun loginUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // User registration successful
                    // You can handle additional steps here, such as sending a verification email.
                    db.collection("users")
                        .document(email)
                        .get()
                        .addOnSuccessListener { document ->
                            if (document.exists()) {
                                // Document found, you can access user data
                                val user = document.toObject(User::class.java)
                                val intent = Intent (this@LoginActivity , MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                                // Handle user data here
                            } else {
                                // Document does not exist
                            }
                        }
                        .addOnFailureListener { e ->
                            // Handle errors here
                        }
                } else {
                    // If registration fails, display a message to the user.
                    try {
                        throw task.exception!!
                    } catch (e: FirebaseAuthUserCollisionException) {
                        // Handle collision (if user with the same email already exists)
                    } catch (e: Exception) {
                        // Handle other exceptions
                    }
                }
            }
    }
}