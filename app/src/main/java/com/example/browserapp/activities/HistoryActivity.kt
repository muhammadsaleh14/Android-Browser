package com.example.browserapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.browserapp.R
import com.example.browserapp.adapters.BookmarksAdapter
import com.example.browserapp.databinding.ActivityHistoryBinding
import com.example.browserapp.adapters.HistoryAdapter
import com.example.browserapp.models.UserBookmark
import com.example.browserapp.models.UserHistory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.leadingspark.fulltkdapp.CustomClasses.SwipeHelper

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding
    private val db = FirebaseFirestore.getInstance()
    private lateinit var historyList: MutableList<UserHistory>
    private val currentUser = FirebaseAuth.getInstance().currentUser
    private val email = currentUser?.email?: ""
    private var historyAdapter = HistoryAdapter(this, mutableListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val toolbar = findViewById<Toolbar>(R.id.my_toolbar)
        setSupportActionBar(toolbar)

        if (supportActionBar != null) {
            supportActionBar?.title = "History"
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        val currentUser = FirebaseAuth.getInstance().currentUser
        val email = currentUser?.email?: ""
        val historyCollection = db.collection("users").document(email).collection("history")

        historyList = mutableListOf()
        historyCollection
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    // Handle errors here
                    return@addSnapshotListener
                }
                historyList = mutableListOf()
                if (snapshot != null) {
                    for (document in snapshot.documents) {

                        // fetching data fields to make a new tournament
                        val historyData = document.data
                        val url = historyData?.get("url") as? String ?: ""
                        val name = historyData?.get("name") as? String ?: ""
                        val timeStamp = document.get("timeStamp") as? Long ?: 0
                        val id = document.id as? String ?: ""
                        val history = UserHistory(id,url,name, timeStamp)
                        historyList.add(history)
                    }
                    val temp = historyList.sortedByDescending { it.timestamp }
                    historyList = temp.toMutableList()
                    historyAdapter.updateList(historyList)
                    historyAdapter.notifyDataSetChanged()
                }
            }
        binding.historyRecyclerView.adapter = historyAdapter

        val itemTouchHelper = ItemTouchHelper(object : SwipeHelper(binding.historyRecyclerView) {
            override fun instantiateUnderlayButton(position: Int): List<UnderlayButton> {
                var buttons = listOf<UnderlayButton>()
                val deleteButton = deleteButton(position)
                buttons = listOf(deleteButton)
                return buttons
            }
        })

        itemTouchHelper.attachToRecyclerView(binding.historyRecyclerView)




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
                    db.collection("users").document(email).collection("history")
                        .document(historyList[position].key).delete()

                    historyAdapter.notifyDataSetChanged()
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