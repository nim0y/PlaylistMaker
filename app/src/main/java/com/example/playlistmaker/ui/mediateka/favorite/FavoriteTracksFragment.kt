package com.example.playlistmaker.ui.mediateka.favorite

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentFavoriteTracksBinding
import com.example.playlistmaker.domain.models.search.Track
import com.example.playlistmaker.ui.search.TrackAdapter
import com.example.playlistmaker.utils.SEARCH_QUERY_HISTORY
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteTracksFragment : Fragment() {

    private var _binding: FragmentFavoriteTracksBinding? = null
    private val binding get() = _binding!!
    private var favoriteAdapter = TrackAdapter()
    private val vm by viewModel<FavoriteTracksViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteTracksBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.favoriteRecyclerView.adapter = favoriteAdapter

        favoriteAdapter.itemClickListener = {
            openAudioPlayer(track = it)
        }

        vm.favoriteState.observe(viewLifecycleOwner) {
            execute(it)
        }
    }

    private fun execute(favoriteState: FavoriteState) {
        when (favoriteState) {
            is FavoriteState.Content -> showFavoritesList(favoriteState.tracks)
            is FavoriteState.NoEntry -> showBlank()
            is FavoriteState.Load -> showLoading()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showFavoritesList(tracks: List<Track>) {
        binding.favoriteListEmpty.visibility = View.GONE
        binding.favoritesProgressBar.visibility = View.GONE
        binding.favoriteRecyclerView.visibility = View.VISIBLE
        with(favoriteAdapter) {
            tracksList.clear()
            tracksList.addAll(tracks)
            notifyDataSetChanged()                       //*notifyItemRangeChanged(0, tracks.size) - cause crash ask mentor!
        }
    }

    private fun showBlank() {
        binding.favoriteListEmpty.visibility = View.VISIBLE
        binding.favoritesProgressBar.visibility = View.GONE
        binding.favoriteRecyclerView.visibility = View.GONE
    }

    private fun showLoading() {
        binding.favoriteListEmpty.visibility = View.GONE
        binding.favoritesProgressBar.visibility = View.VISIBLE
        binding.favoriteRecyclerView.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        vm.getFavoriteList()
    }

    private fun openAudioPlayer(track: Track) {
        val bundle = Bundle()
        bundle.putParcelable(SEARCH_QUERY_HISTORY, track)
        findNavController().navigate(
            R.id.action_mediatekaFragment_to_audioPlayerFragment,
            bundle
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = FavoriteTracksFragment()
    }
}