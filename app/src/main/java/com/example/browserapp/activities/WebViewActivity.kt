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

        val bookmarkOption = findViewById<Button>(R.id.optionbookmarks)
        val historyOption = findViewById<Button>(R.id.optionHistory)
        val logoutOption = findViewById<Button>(R.id.optionLogout)
        val newTabOption = findViewById<Button>(R.id.newTabOption)
        var urlEditEditText = findViewById<EditText>(R.id.searchUrl)
        val webView = findViewById<WebView>(R.id.webView) // Replace with your WebView's ID
        webView.webViewClient = MyWebViewClient()

        // Load a specific URL
        val receivedUrl = intent.getStringExtra("url") ?: ""
        val receivedName = intent.getStringExtra("name") ?: ""
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userEmail = currentUser?.email?: ""

        try {

            val history = UserHistory(receivedUrl,receivedName, System.currentTimeMillis())
            val historyDocument = db.collection("users").document(userEmail).collection("history").document()

            historyDocument.set(history.dictionary)
            urlEditEditText.setText(receivedUrl)
            webView.loadUrl(receivedUrl ?: "")
        } catch (e: Exception) {
            Log.e("TAGINN1", e.stackTraceToString())
        }

        val bookmarkButton = findViewById<ImageButton>(R.id.bookmarksIcon)
        val key = receivedName+receivedUrl
        val documentReference = db.collection("users").document(userEmail).collection("bookmarks").document(key)
        var bookmarked = false
        documentReference.get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val documentSnapshot: DocumentSnapshot? = task.result
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        bookmarkButton.setImageResource(R.drawable.star_bookmark)
                        bookmarked = true
                    }
                }
            }
        bookmarkButton.setOnClickListener{
            // if not already bookmarked
            if(!bookmarked){
                val bookmark = UserBookmark(receivedUrl,receivedName,System.currentTimeMillis())
                val bookmarkDocument = db.collection("users").document(userEmail).collection("bookmarks").document(key)
                bookmarkDocument.set(bookmark.dictionary)
                bookmarked = true
                bookmarkButton.setImageResource(R.drawable.star_bookmark)
            }
            else{
                // if bookmarked already
                db.collection("users").document(userEmail).collection("bookmarks").document(key).delete()
                bookmarkButton.setImageResource(R.drawable.star_empty)
                bookmarked = false
            }
        }
        val showOptionsButton = findViewById<ImageButton>(R.id.showOptionsBtn)
        val optionsListStub = findViewById<ViewStub>(R.id.options_list_stub)
        val optionsList: View? = optionsListStub.inflate()
        if (optionsList != null) {
            optionsList.visibility = View.GONE
        }
        showOptionsButton.setOnClickListener {
            if (optionsList?.visibility == View.GONE) {
                // Slide in the options list
                optionsList.visibility = View.VISIBLE
                val slideInAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_left)
                optionsList.startAnimation(slideInAnimation)
                showOptionsButton.setImageResource(R.drawable.arrow_right)
            } else {
                // Slide out the options list
                val slideOutAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_out_to_left)
                optionsList?.startAnimation(slideOutAnimation)
                optionsList?.visibility = View.GONE
                showOptionsButton.setImageResource(R.drawable.arrow_left)
            }
        }

        bookmarkOption.setOnClickListener{
            val intent = Intent(this@WebViewActivity , BookmarkActivity::class.java)
            startActivity(intent)
        }

        historyOption.setOnClickListener{
            val intent = Intent(this@WebViewActivity , HistoryActivity::class.java)
            startActivity(intent)
        }

        logoutOption.setOnClickListener{
            val auth = FirebaseAuth.getInstance()
            auth.signOut()
            val intent = Intent(this@WebViewActivity , LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        newTabOption.setOnClickListener{
            val intent = Intent(this@WebViewActivity , MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

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