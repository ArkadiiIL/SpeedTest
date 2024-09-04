package com.arkadii.myspeedtest.presentation.speedtest

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.arkadii.myspeedtest.R
import com.arkadii.myspeedtest.databinding.FragmentSpeedTestBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SpeedTestFragment : Fragment() {
    //Переменная для хранения binding объекта с помощью которого можно получить доступ к элементам интерфейса
    private var _binding: FragmentSpeedTestBinding? = null

    //Безопасный дооступ гарантирующий, что binding не будeт null
    private val binding get() = _binding!!

    //Получение viewModel с помощью Hilt
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
        //Устанавливаем слушатели на элементы интерфейса, а так же обрабатываем события полученные из viewModel
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
                //Показываем сообщение об ошибке
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

    //Отражаем сообщение об ошибку с помощью Toast
    private fun showErrorText(context: Context, errorText: String?) {
        val text = errorText ?: context.getText(R.string.unknownError)
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
    }
}




