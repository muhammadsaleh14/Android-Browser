package com.example.browserapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.browserapp.R
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {

    private val splashDisplayLength = 3000 // 3 seconds
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            // Start the next activity (e.g., MainActivity)
            if (isUserAuthenticated()) {
                // User is authenticated, navigate to the main content of the app
                navigateToMainActivity()
            } else {
                // User is not authenticated, navigate to the login screen
                navigateToLoginActivity()
            }
            finish()
        }, splashDisplayLength.toLong())



    }
    private fun isUserAuthenticated(): Boolean {
        return FirebaseAuth.getInstance().currentUser != null
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