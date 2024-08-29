package com.arkadii.myspeedtest.ui.speedtest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.arkadii.myspeedtest.databinding.FragmentSpeedTestBinding

class SpeedTestFragment : Fragment() {

    private var _binding: FragmentSpeedTestBinding? = null

    private val binding get() = _binding!!
    private lateinit var viewModel: SpeedTestViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[SpeedTestViewModel::class.java]

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
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}