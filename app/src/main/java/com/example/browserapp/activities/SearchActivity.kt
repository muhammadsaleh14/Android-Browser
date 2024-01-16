package com.example.browserapp.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewStub
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.browserapp.LoadingAnimation
import com.example.browserapp.R
import com.example.browserapp.databinding.ActivitySearchBinding
import com.example.browserapp.fragments.ImagesFragment
import com.example.browserapp.fragments.VideosFragment
import com.example.browserapp.fragments.WebPagesFragment
import com.example.browserapp.networkManagement.ConnectivityObserver
import com.example.browserapp.networkManagement.NetworkConnectivityObserver
import com.example.browserapp.viewmodels.SearchViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private val supportFragmentManager = getSupportFragmentManager()
    private var currentFragment: Fragment? = null
    private lateinit var connectivityObserver: ConnectivityObserver

    //    companion object:{
//
//    }
    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            Log.d("qqq","inside search")
            super.onCreate(savedInstanceState)
            binding = ActivitySearchBinding.inflate(layoutInflater)
            setContentView(binding.root)
            val toolbar = findViewById<Toolbar>(R.id.my_toolbar)
            setSupportActionBar(toolbar)

            //disabling night mode
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            //get values from search
            val searchTerm = intent.getStringExtra ("searchTerm")
            //assigning ids

            val urlEditText = findViewById<EditText>(R.id.searchUrl)


            //setting view model
            val viewModel = ViewModelProvider(this)[SearchViewModel::class.java]
            viewModel.searchTerm.value = searchTerm
            Log.d("qqq","search term is $searchTerm")
            urlEditText.setText(searchTerm)
            urlEditText.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // Perform search action here
                    val prevVal = viewModel.searchTerm.value
                    val newVal = urlEditText.text.toString()
                    if(prevVal!= newVal){
                        viewModel.webPagesLoaded = false
                        viewModel.imagesLoaded = false
                        viewModel.videosLoaded = false
                    }
                    viewModel.searchTerm.value = newVal
//                    replaceFragment(WebPagesFragment())
//                    replaceFragment(ImagesFragment())
//                    replaceFragment(VideosFragment())
                }
                false
            }
            //Check Connectivity
            connectivityObserver = NetworkConnectivityObserver(applicationContext)
            lifecycleScope.launch {
                connectivityObserver.observe()
                    .collect { status ->
                        showAlert(status,findViewById(android.R.id.content))
                    }
            }
            val initialStatus = connectivityObserver.getCurrentStatus()
            if (initialStatus != ConnectivityObserver.Status.Available) {
                showAlert(initialStatus,findViewById(android.R.id.content))
            }
            //control loading
            val loadingAnimation = LoadingAnimation(binding.loadingAnimation)
            viewModel.isLoading.observe(this) { isLoading ->
                if (isLoading) {
//                    Log.d("qqq", "starting animation")
                    loadingAnimation.startLoading()
                } else {
//                    Log.d("qqq", "stopping animation")
                    loadingAnimation.stopLoading()
                }
            }
//            supportFragmentManager.beginTransaction()
//                .add(R.id.fragmentContainer,WebPagesFragment())
//                .add(R.id.fragmentContainer,ImagesFragment())
//                .add(R.id.fragmentContainer,VideosFragment())
//                .commit()
//            val webPagesFragment = WebPagesFragment()
//            val imagesFragment = ImagesFragment()
//            val videosFragment = VideosFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, WebPagesFragment())
                .commit()
            binding.btnWebPages.setOnClickListener {
                replaceFragment(WebPagesFragment())
//                replaceFragment(WebPagesFragment())
            }
            binding.btnImages.setOnClickListener {
                replaceFragment(ImagesFragment())
//                replaceFragment(ImagesFragment())
            }
//
            binding.btnVideos.setOnClickListener {
                replaceFragment(VideosFragment())
//                replaceFragment(VideosFragment())
            }


//            bookmarkOption.setOnClickListener{
//                val intent = Intent(this@SearchActivity , BookmarkActivity::class.java)
//                startActivity(intent)
//            }
//
//            historyOption.setOnClickListener{
//                val intent = Intent(this@SearchActivity , HistoryActivity::class.java)
//                startActivity(intent)
//            }
//
//            logoutOption.setOnClickListener{
//
        //                val auth = FirebaseAuth.getInstance()
//                auth.signOut()
//                val intent = Intent(this@SearchActivity , LoginActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                startActivity(intent)
//            }
//
//            newTabOption.setOnClickListener{
//                val intent = Intent(this@SearchActivity , MainActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                startActivity(intent)
//            }

        } catch (e: Exception) {
            Log.e("TAGINN", e.stackTraceToString())
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.my_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.bookmarkOption -> {
                // Handle option 1 click
                val intent = Intent(this@SearchActivity , BookmarkActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.historyOption -> {
                // Handle option 2 click
                val intent = Intent(this@SearchActivity , HistoryActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.newTabOption -> {
                // Handle option 1 click
                val intent = Intent(this@SearchActivity , MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                return true
            }
            R.id.logoutOption -> {
                // Handle option 2 click
                val auth = FirebaseAuth.getInstance()
                auth.signOut()
                val intent = Intent(this@SearchActivity , LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun replaceFragment(fragment: Fragment) {
        try {
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragmentContainer, fragment)
            fragmentTransaction.commit()
        } catch (e: Exception) {
            Log.e("TAGINN4", e.stackTraceToString())
        }
    }

    private fun showFragment(newFragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, newFragment)
            .addToBackStack(null)
            .commit()
    }

    //alert for network
    companion object {
        fun showAlert(status: ConnectivityObserver.Status,view:View) {
            val message = when (status) {
                ConnectivityObserver.Status.Available -> "Network is available"
                ConnectivityObserver.Status.Unavailable -> "Network is unavailable"
                else -> "Unknown network status"
            }
            Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
        }

    }

}


