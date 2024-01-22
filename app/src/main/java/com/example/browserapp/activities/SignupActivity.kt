package com.example.browserapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
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
        binding.progressBar.visibility = View.INVISIBLE

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
            else if(_password.length<6){
                Toast.makeText(this, "Weak Password , Try Again", Toast.LENGTH_SHORT).show()
            }
            else{
                binding.progressBar.visibility = View.VISIBLE
                val user = User(_email)
                registerUser(user,_password)
                binding.progressBar.visibility = View.INVISIBLE
            }

        }
        button.setOnClickListener {
            finish()
        }
    }

    private fun sendVerificationEmail() {
        val firebaseuser = FirebaseAuth.getInstance().currentUser
        firebaseuser?.sendEmailVerification()
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Email sent successfully
                    // Display success message to user
                    Toast.makeText(this, "Verification email sent", Toast.LENGTH_SHORT).show()
                    // User data added to Firestore successfully
//                    val intent = Intent (this@SignupActivity , MainActivity::class.java)
//                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                    startActivity(intent)

                    val auth = FirebaseAuth.getInstance()
                    auth.signOut()
                    finish()
                } else {
                    // Handle error
                    Toast.makeText(this, "Email could not be verified", Toast.LENGTH_SHORT).show()
                    binding.progressBar.visibility = View.INVISIBLE
                }
            }
    }
    fun registerUser(user:User, password: String) {

        auth.createUserWithEmailAndPassword(user.email, password)
            .addOnCompleteListener(this) {task ->

                if (task.isSuccessful) {
                    binding.progressBar.visibility = View.VISIBLE
                    sendVerificationEmail()

            } else {
                    Log.d("signuppp","inside task not successful")
//                Toast.makeText(this, "Fill up both email and password", Toast.LENGTH_SHORT).show()
                try {
                    throw task.exception!!
                } catch (e: FirebaseAuthUserCollisionException) {
                    Toast.makeText(this, "User with this email already exists", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    // Handle other exceptions
                }
            }
            }
    }
}