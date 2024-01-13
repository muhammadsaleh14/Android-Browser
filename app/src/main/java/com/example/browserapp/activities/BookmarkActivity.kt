package com.example.browserapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.browserapp.databinding.ActivityBookmarkBinding
import com.example.browserapp.adapters.BookmarksAdapter
import com.example.browserapp.models.Bookmark
import com.example.browserapp.models.History
import com.google.firebase.firestore.FirebaseFirestore

class BookmarkActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBookmarkBinding
    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookmarkBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        if (supportActionBar != null) {
            supportActionBar?.title = "Bookmarks"
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        val email = intent.getStringExtra("email") ?: ""
        val bookmarksCollection = db.collection("users").document(email).collection("bookmarks")

        var bookmarks: MutableList<Bookmark>
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
                        val bookmark = Bookmark(url,name, timeStamp)
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
}