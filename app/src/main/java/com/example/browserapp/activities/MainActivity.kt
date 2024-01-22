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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.example.browserapp.R
import com.example.browserapp.adapters.BookmarksAdapter
import com.example.browserapp.models.UserBookmark
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

// TODO: image not showing
class MainActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    private var bookmarksAdapter = BookmarksAdapter(this, mutableListOf())
    private lateinit var bookmarks: MutableList<UserBookmark>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.my_toolbar)
        setSupportActionBar(toolbar)
        val user= FirebaseAuth.getInstance().currentUser
        FirebaseAuth.getInstance().addAuthStateListener { auth ->
            val user = auth.currentUser
            if (user == null) {
                // User is no longer authenticated, navigate to login screen
                navigateToLoginActivity()
                finish()
            }
        }



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
            searchEditText.setText("")
        }
        val bookmarksCollection = db.collection("users").document(email?:"").collection("bookmarks")

        bookmarks = mutableListOf()

        bookmarksCollection
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    // Handle errors here
                    return@addSnapshotListener
                }
                bookmarks = mutableListOf()
                if (snapshot != null) {
                    for (document in snapshot.documents) {

                        // fetching data fields to make a new tournament
                        val bookmarkData = document.data
                        val url = bookmarkData?.get("url") as? String ?: ""
                        val name = bookmarkData?.get("name") as? String ?: ""
                        val timeStamp = document.get("timeStamp") as? Long ?: 0
                        val key = document.id as? String ?: ""
                        val bookmark = UserBookmark(key,url,name, timeStamp)
                        bookmarks.add(bookmark)
                    }
                    val temp = bookmarks.sortedByDescending { it.timestamp }
                    bookmarks = temp.toMutableList()
                    bookmarksAdapter.updateList(bookmarks)
                    bookmarksAdapter.notifyDataSetChanged()
                }
            }
        val bookmarksRecyclerView = findViewById<RecyclerView>(R.id.bookmarksRV)
        bookmarksRecyclerView.adapter = bookmarksAdapter

        val sharedPreferences = getSharedPreferences("preferences", MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putLong("lastLoginTime", System.currentTimeMillis())
            apply()
        }

        FirebaseAuth.getInstance().addAuthStateListener { auth ->
            if (auth.currentUser != null) {
                // Check last login time and calculate difference
                val sharedPreferences = getSharedPreferences("preferences", MODE_PRIVATE)
                val lastLoginTime = sharedPreferences.getLong("lastLoginTime", 0L)
                val now = System.currentTimeMillis()
                val timeDifference = now - lastLoginTime
                if (timeDifference > 10 * 24 * 60 * 60 * 1000) { // 5 days in milliseconds
                    // User inactive for 5 days, prompt for re-login
                    showLoginDialog()
                } else {
                    // Update last login time if necessary
                    with(sharedPreferences.edit()) {
                        putLong("lastLoginTime", now)
                        apply()
                    }
                }
            }
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

    private fun showLoginDialog() {
        val dialog = AlertDialog.Builder(this)
            .setTitle("Please Re-login")
            .setMessage("You haven't logged in for 5 days. Please sign in again to continue using the app.")
            .setPositiveButton("Login") { dialog, _ ->
                // Start your login activity or fragment
                val auth = FirebaseAuth.getInstance()
                auth.signOut()
                navigateToLoginActivity()
                finish()
            }
            .setCancelable(false) // Prevent dismissing the dialog without logging in
            .create()
        dialog.show()
    }

    private fun navigateToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
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
        return super.onOptionsItemSelected(item)
    }

}
