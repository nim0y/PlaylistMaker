package com.example.playlistmaker.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private val vm: SettingsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.switchOnState.observe(viewLifecycleOwner) { isChecked ->
            binding.switchcompat.isChecked = isChecked
        }
        binding.switchcompat.setOnCheckedChangeListener { _, isChecked ->
            vm.onSwitchClicked(isChecked)
        }

        binding.shareButton.setOnClickListener {
            vm.onShare()
        }

        binding.supportButton.setOnClickListener {
            vm.onSupport()
        }

        binding.termsOfUseButton.setOnClickListener {
            vm.onTermsOfUse()
        }
    }
}