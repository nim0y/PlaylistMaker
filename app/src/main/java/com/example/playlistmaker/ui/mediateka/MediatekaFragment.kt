package com.example.playlistmaker.ui.mediateka

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentMediatekaBinding

import com.google.android.material.tabs.TabLayoutMediator

class MediatekaFragment : Fragment() {

    private lateinit var binding: FragmentMediatekaBinding
    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMediatekaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPager.adapter = ViewPagerAdapter(childFragmentManager, lifecycle)

        tabMediator =
            TabLayoutMediator(binding.tabLayoutMediateka, binding.viewPager) { tab, position ->
                when (position) {
                    0 -> tab.text = resources.getString(R.string.favorite_tracks)
                    else -> tab.text = resources.getString(R.string.playlists)
                }
            }
        tabMediator.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tabMediator.detach()
    }

}