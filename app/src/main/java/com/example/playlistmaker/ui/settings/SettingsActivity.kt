package com.example.playlistmaker.ui.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {

    private var binding: ActivitySettingsBinding? = null
    private val vm: SettingsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        vm.switchOnState.observe(this) { isChecked ->
            binding?.switchcompat?.isChecked = isChecked
        }

        binding?.toolbarsettings?.setNavigationOnClickListener {
            finish()
        }
        binding?.switchcompat?.setOnCheckedChangeListener { _, isChecked ->
            vm.onSwitchClicked(isChecked)
        }

        binding?.shareButton?.setOnClickListener {
            vm.onShare()
        }

        binding?.supportButton?.setOnClickListener {
            vm.onSupport()
        }

        binding?.termsOfUseButton?.setOnClickListener {
            vm.onTermsOfUse()
        }
    }
}