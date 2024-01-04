package com.example.browserapp.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.browserapp.R
import com.example.browserapp.databinding.ActivityFragmentsBinding
import com.example.browserapp.searchfragments.ImagesFragment
import com.example.browserapp.searchfragments.VideosFragment
import com.example.browserapp.searchfragments.WebPagesFragment

class FragmentsActivity : AppCompatActivity() {
    lateinit var binding: ActivityFragmentsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = ActivityFragmentsBinding.inflate(layoutInflater)
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
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer, fragment)
        fragmentTransaction.commit()

    }
}


