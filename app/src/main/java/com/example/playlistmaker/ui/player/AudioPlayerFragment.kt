package com.example.playlistmaker.ui.player

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentAudioPlayerBinding
import com.example.playlistmaker.domain.models.search.Playlist
import com.example.playlistmaker.domain.models.search.Track
import com.example.playlistmaker.domain.models.search.player.FavoriteState
import com.example.playlistmaker.domain.models.search.player.PlayerState
import com.example.playlistmaker.domain.models.search.playlist.PlaylistState
import com.example.playlistmaker.ui.MainActivity
import com.example.playlistmaker.utils.SEARCH_QUERY_HISTORY
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class AudioPlayerFragment : Fragment(), AudioPlayerViewHolder.ClickListener {

    private var _binding: FragmentAudioPlayerBinding? = null
    private val binding get() = _binding!!
    private lateinit var track: Track
    private lateinit var trackPreviewUrl: String
    private val vm by viewModel<AudioPlayerViewModel>()
    private lateinit var adapter: AudioPlayerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAudioPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        track = arguments?.getParcelable<Track>(SEARCH_QUERY_HISTORY) as Track

        (activity as MainActivity).hideNavBar()

        showTrack(track)

        adapter = AudioPlayerAdapter(this)

        binding.bottomSheetRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.bottomSheetRecyclerView.adapter = adapter

        val bottomSheetState = BottomSheetBehavior.from(binding.bottomSheetAudioPlayer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }
        bottomSheetState.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            @SuppressLint("NotifyDataSetChanged")
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.bottomSheetOverlay.visibility = View.GONE
                    }

                    else -> {
                        adapter.notifyDataSetChanged()
                        binding.bottomSheetOverlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        binding.buttonAddTrack.setOnClickListener {
            bottomSheetState.state = BottomSheetBehavior.STATE_COLLAPSED
            adapter.notifyDataSetChanged()
        }

        binding.bottomSheetAddPlaylistBtn.setOnClickListener {
            findNavController().navigate(R.id.action_audioPlayerFragment_to_newPlaylistFragment)
        }

        vm.isFavorite(track)

        vm.getPlaylists()

        vm.audioPlayerState.observe(viewLifecycleOwner) { state ->
            binding.playerTime.text =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(state.timerValue)
            execute(state.playerState)
        }

        vm.playlistState.onEach { state -> playlistStateManage(state) }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        vm.favoriteState.observe(viewLifecycleOwner) { isFavorite ->
            like(isFavorite)
        }

        binding.buttonLike.setOnClickListener {
            vm.clickOnFavorite(track)
        }

        binding.buttonPlay.setOnClickListener {
            vm.playControl()
        }

        vm.setPlayer(trackPreviewUrl)

        binding.toolbarplayer.setNavigationOnClickListener {
            findNavController().navigateUp()
            (activity as MainActivity).showNavBar()
        }
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                (activity as? MainActivity)?.showNavBar()
                findNavController().navigateUp()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        vm.onDestroy()
        _binding = null
    }

    override fun onPause() {
        super.onPause()
        vm.onPause()
        binding.buttonPlay.setImageResource(R.drawable.ic_play_button)
    }

    private fun showTrack(track: Track) {
        binding.apply {
            trackName.text = track.trackName
            artistName.text = track.artistName
            trackGenreTv.text = track.primaryGenreName
            albumNameTv.text = track.collectionName
            trackCountryTv.text = track.country
            trackLengthTv.text =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
            trackYearTv.text = LocalDateTime.parse(
                track.releaseDate,
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
            ).year.toString()
        }
        trackPreviewUrl = track.previewUrl.orEmpty()
        binding.icPlayerDiscCover.let {
            Glide.with(this)
                .load(track.artworkUrl512)
                .placeholder(R.drawable.ic_placeholder)
                .centerCrop()
                .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.track_corner_radious)))
                .into(it)
        }
    }

    private fun execute(playerState: PlayerState) {
        when (playerState) {
            PlayerState.PLAYING_STATE -> setIconPause()
            PlayerState.PAUSE_STATE -> setIconPlay()
            PlayerState.PREPARATION_STATE -> setIconPlay()
            PlayerState.DEFAULT_STATE -> {}
        }
    }

    private fun like(isFavoriteState: FavoriteState) {
        when (isFavoriteState) {
            FavoriteState(true) -> binding.buttonLike.setImageResource(R.drawable.ic_like_button_clicked)
            FavoriteState(false) -> binding.buttonLike.setImageResource(R.drawable.ic_like_button)
        }
    }

    private fun setIconPlay() {
        binding.buttonPlay.setImageResource(R.drawable.ic_play_button)
    }

    private fun setIconPause() {
        binding.buttonPlay.setImageResource(R.drawable.ic_play_button_clicked)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun playlistStateManage(state: PlaylistState) {
        when (state) {
            is PlaylistState.Empty -> {
                binding.bottomSheetRecyclerView.visibility = View.GONE
            }

            is PlaylistState.Data -> {
                val playlists = state.tracks
                binding.bottomSheetRecyclerView.visibility = View.VISIBLE
                adapter.playlists = playlists as ArrayList<Playlist>
                adapter.notifyDataSetChanged()
            }

            else -> {}
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onClick(playlist: Playlist) {
        if (!vm.inPlaylist(
                playlist = playlist,
                trackId = track.trackId?.toLong() ?: 0
            )
        ) {
            vm.clickOnAddtoPlaylist(playlist = playlist, track = track)
            Toast.makeText(
                requireContext().applicationContext,
                "${getString(R.string.added_to_playlist)} ${playlist.name}",
                Toast.LENGTH_SHORT
            )
                .show()
            playlist.tracksAmount = playlist.tracksIds.size
            BottomSheetBehavior.from(binding.bottomSheetAudioPlayer).apply {
                state = BottomSheetBehavior.STATE_HIDDEN
            }
        } else {
            Toast.makeText(
                requireContext().applicationContext,
                "${getString(R.string.already_in_playlist)} ${playlist.name}",
                Toast.LENGTH_SHORT
            )
                .show()
        }
        adapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        BottomSheetBehavior.from(binding.bottomSheetAudioPlayer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }
    }
}
