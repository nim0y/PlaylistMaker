package com.example.playlistmaker.ui.mediateka.playlists

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlayListBinding
import com.example.playlistmaker.domain.models.search.Playlist
import com.example.playlistmaker.domain.models.search.playlist.PlaylistState
import com.example.playlistmaker.ui.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayListFragment : Fragment(), PlaylistsViewHolder.ClickListener {

    private var _binding: FragmentPlayListBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: PlaylistsAdapter

    private val vm by viewModel<PlayListViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.noPlaylistImage.visibility = View.GONE
        binding.noPlaylistTxt.visibility = View.GONE

        vm.getPlaylists()


        adapter = PlaylistsAdapter(this)
        binding.playlistsRecycleView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.playlistsRecycleView.adapter = adapter

        vm.playlistState.observe(viewLifecycleOwner) {
            execute(it)
        }

        binding.newPlaylistBtn.setOnClickListener {
            findNavController().navigate(R.id.action_mediatekaFragment_to_newPlaylistFragment)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun execute(state: PlaylistState) {
        when (state) {
            is PlaylistState.Data -> {
                val playlists = state.tracks
                showData()
                adapter.playlists = playlists as ArrayList<Playlist>
                adapter.notifyDataSetChanged()
                (activity as? MainActivity)?.showNavBar()
            }

            is PlaylistState.Empty -> {
                showEmpty()
            }

            is PlaylistState.Load -> {
                binding.playlistsProgressBar.visibility = View.VISIBLE
            }
        }
    }

    private fun showData() {
        binding.playlistsProgressBar.visibility = View.GONE
        binding.noPlaylistImage.visibility = View.GONE
        binding.noPlaylistTxt.visibility = View.GONE
        binding.playlistsRecycleView.visibility = View.VISIBLE
    }

    private fun showEmpty() {
        binding.playlistsProgressBar.visibility = View.GONE
        binding.noPlaylistImage.visibility = View.VISIBLE
        binding.noPlaylistTxt.visibility = View.VISIBLE
        binding.playlistsRecycleView.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = PlayListFragment().apply {
            arguments = Bundle().apply {

            }
        }
    }

    override fun onClick(playlist: Playlist) {
    }
}