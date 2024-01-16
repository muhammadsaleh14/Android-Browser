package com.example.browserapp.activities

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.browserapp.R
import com.example.browserapp.databinding.ActivityBookmarkBinding
import com.example.browserapp.adapters.BookmarksAdapter
import com.example.browserapp.models.UserBookmark
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.leadingspark.fulltkdapp.CustomClasses.SwipeHelper

class BookmarkActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBookmarkBinding
    private val db = FirebaseFirestore.getInstance()
    private lateinit var bookmarks: MutableList<UserBookmark>
    private val currentUser = FirebaseAuth.getInstance().currentUser
    private val email = currentUser?.email?: ""
    private var bookmarksAdapter = BookmarksAdapter(this, mutableListOf())
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
        binding.bookmarksRecyclerView.adapter = bookmarksAdapter

        val itemTouchHelper = ItemTouchHelper(object : SwipeHelper(binding.bookmarksRecyclerView) {
            override fun instantiateUnderlayButton(position: Int): List<UnderlayButton> {
                var buttons = listOf<UnderlayButton>()
                val deleteButton = deleteButton(position)
                buttons = listOf(deleteButton)
                return buttons
            }
        })

        itemTouchHelper.attachToRecyclerView(binding.bookmarksRecyclerView)

    }

    private fun deleteButton(position: Int,
                             ) : SwipeHelper.UnderlayButton {
        return SwipeHelper.UnderlayButton(
            this,
            "Delete",
            14.0f,
            android.R.color.holo_red_light,
            object : SwipeHelper.UnderlayButtonClickListener {
                override fun onClick() {
                    db.collection("users").document(email).collection("bookmarks")
                        .document(bookmarks[position].key).delete()
                    bookmarksAdapter.notifyDataSetChanged()
                }
            })
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