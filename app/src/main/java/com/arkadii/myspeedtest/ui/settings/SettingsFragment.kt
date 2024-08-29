package com.arkadii.myspeedtest.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.arkadii.myspeedtest.databinding.FragmentSettingsBinding
import com.arkadii.myspeedtest.util.Theme

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null

    private val binding get() = _binding!!
    private lateinit var viewModel: SettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[SettingsViewModel::class.java]

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            rbDark.setOnClickListener { updateSettings(Theme.DARK) }
            rbLight.setOnClickListener { updateSettings(Theme.LIGHT) }
            rbSystem.setOnClickListener { updateSettings(Theme.SYSTEM) }

            cbDownload.setOnCheckedChangeListener { _, isChecked -> updateSettings(download = isChecked) }
            cbUpload.setOnCheckedChangeListener { _, isChecked -> updateSettings(upload = isChecked) }
            btnSave.setOnClickListener { updateAndSaveSettings() }
        }

        viewModel.settings.observe(viewLifecycleOwner) { settings ->
            binding.apply {
                when (settings.theme) {
                    Theme.DARK -> rbDark.isChecked = true
                    Theme.LIGHT -> rbLight.isChecked = true
                    Theme.SYSTEM -> rbSystem.isChecked = true
                }
                etServerDownloadUrl.setText(settings.downloadUrl)
                etServerUploadUrl.setText(settings.uploadUrl)
                cbDownload.isChecked = settings.download
                cbUpload.isChecked = settings.upload
            }
        }
    }

    private fun updateSettings(
        theme: Theme = viewModel.settings.value?.theme ?: Theme.SYSTEM,
        downloadUrl: String = binding.etServerDownloadUrl.text.toString(),
        uploadUrl: String = binding.etServerUploadUrl.text.toString(),
        download: Boolean = binding.cbDownload.isChecked,
        upload: Boolean = binding.cbUpload.isChecked
    ) {
        viewModel.updateSettings(theme, downloadUrl, uploadUrl, download, upload)
    }

    private fun updateAndSaveSettings() {
        updateSettings()
        viewModel.saveSettings()
        restartActivity()
    }

    private fun restartActivity() {
        val intent = requireActivity().intent
        requireActivity().finish()
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}