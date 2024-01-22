package com.example.browserapp.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.browserapp.R
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth


class SplashActivity : AppCompatActivity() {

    private val splashDisplayLength = 3000 // 3 seconds
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(
            this
        )
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({
            // Start the next activity (e.g., MainActivity)
            if (isUserAuthenticated()) {
                Log.d("qqq","inside user authenticaed")
                // User is authenticated, navigate to the main content of the app
                navigateToMainActivity()
            } else {
                // User is not authenticated, navigate to the login screen
                Log.d("qqq","inside not user authenticaed")
                navigateToLoginActivity()
            }
            finish()
        }, splashDisplayLength.toLong())



    }
    private fun isUserAuthenticated(): Boolean {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            if (user.isEmailVerified) {
                // Email is verified
                return true
            } else {
                // Email is not verified
                Toast.makeText(this, "V", Toast.LENGTH_SHORT).show()
            }
        }
        return false
    }


    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}