package com.example.browserapp

import android.os.Bundle
import android.view.View
import android.view.ViewStub
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

// TODO: image not showing

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val showOptionsButton = findViewById<ImageButton>(R.id.showOptionsButton)
        val optionsListStub = findViewById<ViewStub>(R.id.options_list_stub)
        val optionsList: View? = optionsListStub.inflate()

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
