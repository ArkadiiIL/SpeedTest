package com.arkadii.myspeedtest.presentation.speedtest

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.arkadii.myspeedtest.R
import com.arkadii.myspeedtest.databinding.FragmentSpeedTestBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SpeedTestFragment : Fragment() {

    private var _binding: FragmentSpeedTestBinding? = null

    private val binding get() = _binding!!
    private val viewModel: SpeedTestViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSpeedTestBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            viewModel.apply {
                btnStart.setOnClickListener { start() }

                instantDownload.observe(viewLifecycleOwner) { speed ->
                    tvInstantDownloadText.text = speed
                }
                averageDownload.observe(viewLifecycleOwner) { speed ->
                    tvDownloadText.text = speed
                }
                instantUpload.observe(viewLifecycleOwner) { speed ->
                    tvInstantUploadText.text = speed
                }
                averageUpload.observe(viewLifecycleOwner) { speed ->
                    tvUploadText.text = speed
                }
                blockStartButton.observe(viewLifecycleOwner) { isBlocked ->
                    btnStart.isEnabled = !isBlocked
                }
                clearFields.observe(viewLifecycleOwner) {
                    tvInstantDownloadText.text = ""
                    tvDownloadText.text = ""
                    tvInstantUploadText.text = ""
                    tvUploadText.text = ""
                }
                showError.observe(viewLifecycleOwner) { errorText ->
                    showErrorText(requireContext(), errorText)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

private fun showErrorText(context: Context, errorText: String?) {
    val text = errorText ?: context.getText(R.string.unknownError)
    Toast.makeText(context, text, Toast.LENGTH_LONG).show()
}
