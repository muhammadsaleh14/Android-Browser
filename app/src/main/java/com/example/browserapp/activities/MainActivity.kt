package com.example.browserapp.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewStub
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.browserapp.R

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
        val searchEditText = findViewById<EditText>(R.id.searchMainEditText)
        val submitSearchBtn = findViewById<Button>(R.id.submitSearchBtn)
        val optionsList: View? = optionsListStub.inflate()
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                submitSearchBtn.isEnabled = !s.isNullOrBlank()
            }
        })
        val email = intent.getStringExtra("email")
        submitSearchBtn.setOnClickListener{
            val intent = Intent(this, SearchActivity::class.java)
            // Put the arguments you want to pass into the Intent
            intent.putExtra("searchTerm", searchEditText.text.toString())
            intent.putExtra("email",email)
            // Start the new Activity
            startActivity(intent)
        }

        optionsList?.visibility = View.GONE  // Initially hide the options list
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
    }

}
