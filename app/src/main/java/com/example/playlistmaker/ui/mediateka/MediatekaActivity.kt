package com.example.playlistmaker.ui.mediateka

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMediatekaBinding
import com.google.android.material.tabs.TabLayoutMediator

class MediatekaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMediatekaBinding
    private lateinit var tabMediator: TabLayoutMediator
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMediatekaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewPager.adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)

        tabMediator =
            TabLayoutMediator(binding.tabLayoutMediateka, binding.viewPager) { tab, position ->
                when (position) {
                    0 -> tab.text = resources.getString(R.string.favorite_tracks)
                    else -> tab.text = resources.getString(R.string.playlists)
                }
            }
        tabMediator.attach()

        binding.toolbarmediateka.setOnClickListener {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}
