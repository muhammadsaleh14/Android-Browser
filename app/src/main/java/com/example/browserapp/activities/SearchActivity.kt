package com.example.browserapp.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.browserapp.R
import com.example.browserapp.databinding.ActivitySearchBinding
import com.example.browserapp.fragments.ImageDetailFragment
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
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer, fragment)
        fragmentTransaction.commit()
    }


}


