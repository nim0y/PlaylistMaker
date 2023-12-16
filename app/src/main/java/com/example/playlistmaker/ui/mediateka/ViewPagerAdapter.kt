package com.example.playlistmaker.ui.mediateka

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.playlistmaker.utils.FRAGMENT_COUNT

class ViewPagerAdapter(fm: FragmentManager, lifecycle: androidx.lifecycle.Lifecycle) :
    FragmentStateAdapter(fm, lifecycle) {

    override fun getItemCount(): Int {
        return FRAGMENT_COUNT
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FavoriteTracksFragment.newInstance()
            else -> PlayListFragment.newInstance()
        }
    }

}