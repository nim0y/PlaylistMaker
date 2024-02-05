package com.example.playlistmaker.ui.mediateka.playlists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentPlayListBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayListFragment : Fragment() {

    private var _binding: FragmentPlayListBinding? = null
    private val binding get() = _binding!!

    private val vm by viewModel<PlayListViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = PlayListFragment()
    }
}


