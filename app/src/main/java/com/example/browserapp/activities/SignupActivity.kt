package com.example.browserapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.example.browserapp.R
import com.example.browserapp.databinding.ActivitySignupBinding
import com.example.browserapp.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestore

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val button: Button = binding.btnLogin
        val signUpButtion: Button = binding.btnSignup

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

        val confirmPasswordEditText: EditText = binding.confirmPasswordEditText
        val confirmPasswordToggle: ImageButton = binding.confirmPasswordToggle

        // Initially, the password is hidden
        confirmPasswordEditText.transformationMethod = PasswordTransformationMethod.getInstance()

        confirmPasswordToggle.setOnClickListener {
            // Toggle password visibility
            if (confirmPasswordEditText.transformationMethod == null) {
                // Password is visible, so hide it
                confirmPasswordEditText.transformationMethod = PasswordTransformationMethod.getInstance()
                passwordToggle.setImageResource(R.drawable.eye_slash_svgrepo_com)
            } else {
                // Password is hidden, so show it
                confirmPasswordEditText.transformationMethod = null
                confirmPasswordToggle.setImageResource(R.drawable.eye_svgrepo_com)
            }

            // Move cursor to the end of the text
            confirmPasswordEditText.setSelection(passwordEditText.text.length)
        }

        signUpButtion.setOnClickListener {
            val _email = binding.txtiptEmail.text.toString()
            val _password = binding.passwordEditText.text.toString()
            val _confirmPassword = binding.confirmPasswordEditText.text.toString()

            if (_email=="" || _password=="" || _confirmPassword=="") {
                Toast.makeText(this, "Fill up all the fields", Toast.LENGTH_SHORT).show()
            }
            else if(_password!= _confirmPassword){
                Toast.makeText(this, "Passwords don't match", Toast.LENGTH_SHORT).show()
            }
            else{
                val user = User(_email,"hehe")
                registerUser(user,_password)
            }

        }
        button.setOnClickListener {
            finish()
        }
    }
    fun registerUser(user:User, password: String) {

        auth.createUserWithEmailAndPassword(user.email, password)
            .addOnCompleteListener(this) {task ->

                if (task.isSuccessful) {
                db.collection("users")
                    .document(user.email)
                    .set(user.dictionary)
                    .addOnSuccessListener {
                        Log.d("signuppp","inside success listener")
                        // User data added to Firestore successfully
                        val intent = Intent (this@SignupActivity , MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Signup Failed. Please try again.", Toast.LENGTH_SHORT).show()
                        // Handle errors here
                    }
            } else {
                    Log.d("signuppp","inside task not successful")
//                Toast.makeText(this, "Fill up both email and password", Toast.LENGTH_SHORT).show()
                try {
                    throw task.exception!!
                } catch (e: FirebaseAuthUserCollisionException) {
                    Toast.makeText(this, "User with this already exists", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    // Handle other exceptions
                }
            }
            }
    }
}