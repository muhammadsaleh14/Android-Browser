package com.example.browserapp.activities

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewStub
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.browserapp.R
import com.example.browserapp.models.UserBookmark
import com.example.browserapp.models.UserHistory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Stack

class WebViewActivity : AppCompatActivity() {
    private val urlStack = Stack<String>()
    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webpage)
        val toolbar = findViewById<Toolbar>(R.id.my_toolbar)
        setSupportActionBar(toolbar)


        class MyWebViewClient : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                // Update the EditText with the new URL
                val editText =
                    findViewById<EditText>(R.id.searchUrl) // Replace with your EditText's ID
                editText.setText(url)
                if (!urlStack.contains(url)) {
                    urlStack.push(url)
                }
            }
        }


        var urlEditText = findViewById<EditText>(R.id.searchUrl)



        urlEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val intent = Intent(this, SearchActivity::class.java)
                // Put the arguments you want to pass into the Intent
                intent.putExtra("searchTerm", urlEditText.text.toString())
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                // Start the new Activity
                startActivity(intent)
            }
            false
        }

        val webView = findViewById<WebView>(R.id.webView) // Replace with your WebView's ID
        webView.webViewClient = MyWebViewClient()

        // Load a specific URL
        val receivedUrl = intent.getStringExtra("url") ?: ""
        val receivedName = intent.getStringExtra("name") ?: ""
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userEmail = currentUser?.email?: ""
        val currentTime = System.currentTimeMillis()
        val key = receivedName
        try {
            val historyDocument = db.collection("users").document(userEmail).collection("history").document()
            val history = UserHistory(historyDocument.id,receivedUrl,receivedName, System.currentTimeMillis())


            historyDocument.set(history.dictionary)
            urlEditText.setText(receivedUrl)
            webView.loadUrl(receivedUrl ?: "")
        } catch (e: Exception) {
            Log.e("TAGINN1", e.stackTraceToString())
        }

        val bookmarkButton = findViewById<ImageButton>(R.id.bookmarksIcon)

        val documentCollection = db.collection("users").document(userEmail).collection("bookmarks")
        var bookmarked = false
        var bookmarkId = ""

        documentCollection
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    // Handle errors here
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    bookmarkId = ""
                    bookmarked = false
                    for (document in snapshot.documents) {
                        val bookmarkData = document.data
                        val url = bookmarkData?.get("url") as? String ?: ""
                        if ( url == receivedUrl){
                            bookmarkId = document.id
                            bookmarkButton.setImageResource(R.drawable.star_bookmark)
                            bookmarked = true
                            break
                        }
                    }
                    if (bookmarkId == ""){
                        bookmarkButton.setImageResource(R.drawable.star_empty)
                    }
                }
            }


        bookmarkButton.setOnClickListener{
            // if not already bookmarked
            if(!bookmarked){

                val bookmarkDocument = db.collection("users").document(userEmail).collection("bookmarks").document()
                val bookmark = UserBookmark(bookmarkDocument.id,receivedUrl,receivedName,System.currentTimeMillis())
                bookmarkId = bookmarkDocument.id
                bookmarkDocument.set(bookmark.dictionary)
                bookmarked = true
                bookmarkButton.setImageResource(R.drawable.star_bookmark)
            }
            else{
                // if bookmarked already
                db.collection("users").document(userEmail).collection("bookmarks").document(bookmarkId).delete()
                bookmarkButton.setImageResource(R.drawable.star_empty)
                bookmarkId = ""
                bookmarked = false
            }
        }



//        bookmarkOption.setOnClickListener{
//            val intent = Intent(this@WebViewActivity , BookmarkActivity::class.java)
//            startActivity(intent)
//        }
//
//        historyOption.setOnClickListener{
//            val intent = Intent(this@WebViewActivity , HistoryActivity::class.java)
//            startActivity(intent)
//        }
//
//        logoutOption.setOnClickListener{
//            val auth = FirebaseAuth.getInstance()
//            auth.signOut()
//            val intent = Intent(this@WebViewActivity , LoginActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            startActivity(intent)
//        }
//
//        newTabOption.setOnClickListener{
//            val intent = Intent(this@WebViewActivity , MainActivity::class.java)
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
                val intent = Intent(this@WebViewActivity , BookmarkActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.historyOption -> {
                // Handle option 2 click
                val intent = Intent(this@WebViewActivity , HistoryActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.newTabOption -> {
                // Handle option 1 click
                val intent = Intent(this@WebViewActivity , MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                return true
            }
            R.id.logoutOption -> {
                // Handle option 2 click
                val auth = FirebaseAuth.getInstance()
                auth.signOut()
                val intent = Intent(this@WebViewActivity , LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onBackPressed() {
        val webView = findViewById<WebView>(R.id.webView) // Replace with your WebView's ID
//        if (webView.canGoBack()) {
//
//            // If WebView can go back, use its history
//            webView.goBack()
//        } else
        if (urlStack.size > 1) {
            // If WebView can't go back, but stack has URLs, pop and load
            urlStack.pop()
            val previousUrl = urlStack.pop()
            webView.loadUrl(previousUrl)

        } else {
            // If both WebView history and stack are empty, go to previous activity
            super.onBackPressed()
        }
    }
}