package com.example.playlistmaker.ui.mediateka.playlists

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentCreatePlaylistBinding
import com.example.playlistmaker.domain.models.search.Playlist
import com.example.playlistmaker.ui.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewPlaylistFragment : Fragment() {
    private var _binding: FragmentCreatePlaylistBinding? = null
    private val binding get() = _binding!!
    private var coverUriSelect: Uri? = null

    private val vm by viewModel<NewPlaylistViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreatePlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).hideNavBar()

        vm.savedCoverUri.observe(viewLifecycleOwner, Observer { savedUri ->
            coverUriSelect = savedUri
        })

        val chooseCover =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { previewUri ->
                if (previewUri != null) {
                    vm.saveCoverToStorage(previewUri, requireContext())
                    binding.newPlaylistCover.setImageURI(previewUri)
                } else {
                }
            }

        binding.newPlaylistCover.setOnClickListener {
            chooseCover.launch(PickVisualMediaRequest((ActivityResultContracts.PickVisualMedia.ImageOnly)))
        }

        binding.createButton.setOnClickListener {
            newPlaylistAdd(coverUriSelect)
            Toast.makeText(
                requireContext(),
                "PLAYLIST Created",
                Toast.LENGTH_SHORT
            ).show()
            findNavController().navigateUp()
        }

        binding.newPlaylistNameEditTxt.doOnTextChanged { text, start, before, count ->
            if (text!!.isNotEmpty()) {
                val color = ContextCompat.getColor(requireContext(), R.color.background_main)
                binding.createButton.isEnabled = true
                binding.createButton.setBackgroundColor(color)
            } else {
                val color = ContextCompat.getColor(requireContext(), R.color.yandex_light_gray)
                binding.createButton.isEnabled = false
                binding.createButton.setBackgroundColor(color)
            }

        }

        binding.toolbarNewPlaylist.setNavigationOnClickListener {
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

    private fun newPlaylistAdd(coverUri: Uri?) {
        val playlistName = binding.newPlaylistNameEditTxt.text.toString()
        val playlistDescription = binding.newPlaylistDescriptionEditTxt.text.toString()
        val coverPath = vm.getCover()

        vm.addPlaylist(
            Playlist(
                id = 0,
                name = playlistName,
                description = playlistDescription,
                coverPath = coverPath,
                tracksIds = arrayListOf(),
                tracksAmount = 0,
                imageUri = coverUri.toString()
            )
        )
    }
}