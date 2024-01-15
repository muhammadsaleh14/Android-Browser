package com.example.browserapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.example.browserapp.R
import com.example.browserapp.databinding.ActivityBookmarkBinding
import com.example.browserapp.adapters.BookmarksAdapter
import com.example.browserapp.models.UserBookmark
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class BookmarkActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBookmarkBinding
    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookmarkBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val toolbar = findViewById<Toolbar>(R.id.my_toolbar)
        setSupportActionBar(toolbar)

        if (supportActionBar != null) {
            supportActionBar?.title = "Bookmarks"
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        val currentUser = FirebaseAuth.getInstance().currentUser
        val email = currentUser?.email?: ""
        val bookmarksCollection = db.collection("users").document(email).collection("bookmarks")

        var bookmarks: MutableList<UserBookmark>
        var bookmarksAdapter = BookmarksAdapter(this, mutableListOf())
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
                        val bookmark = UserBookmark(url,name, timeStamp)
                        bookmarks.add(bookmark)
                    }
                    val temp = bookmarks.sortedBy { it.timestamp }
                    bookmarks = temp.toMutableList()
                    bookmarksAdapter.updateList(bookmarks)
                    bookmarksAdapter.notifyDataSetChanged()
                }
            }
        binding.bookmarksRecyclerView.adapter = bookmarksAdapter

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                // Handle the back button click here
                onBackPressedDispatcher.onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}