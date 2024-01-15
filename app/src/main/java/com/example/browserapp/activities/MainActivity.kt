package com.example.browserapp.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewStub
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import com.example.browserapp.R
import com.google.firebase.auth.FirebaseAuth

// TODO: image not showing
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_main)

        val bookmarksButton = findViewById<ImageButton>(R.id.bookmarksIcon)
        bookmarksButton.visibility = View.GONE
        val showOptionsButton = findViewById<ImageButton>(R.id.showOptionsBtn)
        val optionsListStub = findViewById<ViewStub>(R.id.options_list_stub)
        val bookmarkOption = findViewById<Button>(R.id.optionbookmarks)
        val historyOption = findViewById<Button>(R.id.optionHistory)
        val logoutOption = findViewById<Button>(R.id.optionLogout)
        val searchEditText = findViewById<EditText>(R.id.searchMainEditText)
        val submitSearchBtn = findViewById<Button>(R.id.submitSearchBtn)

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                submitSearchBtn.isEnabled = !s.isNullOrBlank()
            }
        })
        val currentUser = FirebaseAuth.getInstance().currentUser
        val email = currentUser?.email
        submitSearchBtn.setOnClickListener{
            val intent = Intent(this, SearchActivity::class.java)
            // Put the arguments you want to pass into the Intent
            intent.putExtra("searchTerm", searchEditText.text.toString())
            intent.putExtra("email",email)
            // Start the new Activity
            startActivity(intent)
        }

          // Initially hide the options list

//        bookmarkOption.setOnClickListener{
//            val intent = Intent(this@MainActivity , BookmarkActivity::class.java)
//            startActivity(intent)
//        }
//
//        historyOption.setOnClickListener{
//            val intent = Intent(this@MainActivity , HistoryActivity::class.java)
//            startActivity(intent)
//        }
//
//        logoutOption.setOnClickListener{
//            val auth = FirebaseAuth.getInstance()
//            auth.signOut()
//            val intent = Intent(this@MainActivity , LoginActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            startActivity(intent)
//        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.my_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.bookmarkOption -> {
                // Handle option 1 click
                val intent = Intent(this@MainActivity , BookmarkActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.historyOption -> {
                // Handle option 2 click
                val intent = Intent(this@MainActivity , HistoryActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.newTabOption -> {
                // Handle option 1 click

                return true
            }
            R.id.logoutOption -> {
                // Handle option 2 click
                val auth = FirebaseAuth.getInstance()
                auth.signOut()
                val intent = Intent(this@MainActivity , LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                return true
            }
        }

        bookmarkOption.setOnClickListener{
            val intent = Intent(this@MainActivity , BookmarkActivity::class.java)
            startActivity(intent)
        }

        historyOption.setOnClickListener{
            val intent = Intent(this@MainActivity , HistoryActivity::class.java)
            startActivity(intent)
        }

        logoutOption.setOnClickListener{
            val auth = FirebaseAuth.getInstance()
            auth.signOut()
            val intent = Intent(this@MainActivity , LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }


    }

}
