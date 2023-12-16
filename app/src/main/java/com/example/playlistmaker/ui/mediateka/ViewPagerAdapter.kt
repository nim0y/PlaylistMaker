package com.example.playlistmaker.ui.mediateka

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fm: FragmentManager, lifecycle: androidx.lifecycle.Lifecycle) :
    FragmentStateAdapter(fm, lifecycle) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FavoriteTracksFragment.newInstance()
            else -> PlayListFragment.newInstance()
        }
    }

}