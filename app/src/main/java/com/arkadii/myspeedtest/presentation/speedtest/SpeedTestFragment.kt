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
        //Устанавливаем сохранненнвые значения текстовых полей
        savedInstanceState?.let {
            binding.apply {
                tvInstantDownloadText.text = it.getString(KEY_INSTANT_DOWNLOAD_TEXT)
                tvDownloadText.text = it.getString(KEY_AVERAGE_DOWNLOAD_TEXT)
                tvInstantUploadText.text = it.getString(KEY_INSTANT_UPLOAD_TEXT)
                tvUploadText.text = it.getString(KEY_AVERAGE_UPLOAD_TEXT)
            }
        }

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
                //Очищаем поля от информации
                clearFields.observe(viewLifecycleOwner) {
                    if (isButtonClicked) {
                        tvInstantDownloadText.text = ""
                        tvDownloadText.text = ""
                        tvInstantUploadText.text = ""
                        tvUploadText.text = ""
                    }
                }
                //Показываем сообщение об ошибке
                showError.observe(viewLifecycleOwner) { errorText ->
                    showErrorText(requireContext(), errorText)
                }
            }
        }
    }

    //Сохраняем сосстояние текстовых полей
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.apply {
            binding.apply {
                putString(KEY_INSTANT_DOWNLOAD_TEXT, tvInstantDownloadText.text.toString())
                putString(KEY_AVERAGE_DOWNLOAD_TEXT, tvDownloadText.text.toString())
                putString(KEY_INSTANT_UPLOAD_TEXT, tvInstantUploadText.text.toString())
                putString(KEY_AVERAGE_UPLOAD_TEXT, tvUploadText.text.toString())
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

    companion object {
        private const val KEY_INSTANT_DOWNLOAD_TEXT = "instantDownloadText"
        private const val KEY_AVERAGE_DOWNLOAD_TEXT = "averageDownloadText"
        private const val KEY_INSTANT_UPLOAD_TEXT = "instantUploadText"
        private const val KEY_AVERAGE_UPLOAD_TEXT = "averageUploadText"
    }
}




