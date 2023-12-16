package com.example.playlistmaker.ui.mediateka

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentPlayListBinding

class PlayListFragment : Fragment() {

    private var _binding: FragmentPlayListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayListBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    companion object {
        fun newInstance() = PlayListFragment()
    }
}


