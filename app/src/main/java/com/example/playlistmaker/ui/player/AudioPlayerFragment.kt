package com.example.playlistmaker.ui.player

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentAudioPlayerBinding
import com.example.playlistmaker.domain.models.search.Track
import com.example.playlistmaker.domain.models.search.player.FavoriteState
import com.example.playlistmaker.domain.models.search.player.PlayerState
import com.example.playlistmaker.ui.MainActivity
import com.example.playlistmaker.utils.SEARCH_QUERY_HISTORY
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class AudioPlayerFragment : Fragment() {

    private var _binding: FragmentAudioPlayerBinding? = null
    private val binding get() = _binding!!
    private lateinit var track: Track
    private lateinit var trackPreviewUrl: String
    private val vm by viewModel<AudioPlayerViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAudioPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        track = arguments?.getParcelable<Track>(SEARCH_QUERY_HISTORY) as Track

        (activity as MainActivity).hideNavBar()

        showTrack(track)


        vm.isFavorite(track)

        vm.audioPlayerState.observe(viewLifecycleOwner) { state ->
            binding.playerTime.text =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(state.timerValue)
            execute(state.playerState)
        }

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
}