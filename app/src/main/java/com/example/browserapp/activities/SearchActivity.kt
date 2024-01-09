package com.example.browserapp.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewStub
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.browserapp.R
import com.example.browserapp.databinding.ActivitySearchBinding
import com.example.browserapp.fragments.ImagesFragment
import com.example.browserapp.fragments.VideosFragment
import com.example.browserapp.fragments.WebPagesFragment

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            binding = ActivitySearchBinding.inflate(layoutInflater)
            setContentView(binding.root)
            binding.btnWebPages.setOnClickListener {
                replaceFragment(WebPagesFragment())
            }
            binding.btnImages.setOnClickListener {
                replaceFragment(ImagesFragment())
            }
//
            binding.btnVideos.setOnClickListener {
                replaceFragment(VideosFragment())
            }
        }catch (e:Exception){
            Log.e("TAGINN",e.stackTraceToString())
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
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer, fragment)
        fragmentTransaction.commit()
    }


}


