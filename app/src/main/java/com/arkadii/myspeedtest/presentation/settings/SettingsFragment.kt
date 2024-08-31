package com.arkadii.myspeedtest.presentation.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.arkadii.myspeedtest.databinding.FragmentSettingsBinding
import com.arkadii.myspeedtest.util.Theme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment() {
    //Переменная для хранения binding объекта с помощью которого можно получить доступ к элементам интерфейса
    private var _binding: FragmentSettingsBinding? = null
    //Безопасный дооступ гарантирующий, что binding не будте null
    private val binding get() = _binding!!
    //Получение viewModel с помощью Hilt
    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            //Устанавка обработчика нажаатия на кнопки выбора темы
            rbDark.setOnClickListener { updateSettings(Theme.DARK) }
            rbLight.setOnClickListener { updateSettings(Theme.LIGHT) }
            rbSystem.setOnClickListener { updateSettings(Theme.SYSTEM) }

            //Установка обработчиков чекбоксов для выбора режима тестирования
            cbDownload.setOnCheckedChangeListener { _, isChecked -> updateSettings(download = isChecked) }
            cbUpload.setOnCheckedChangeListener { _, isChecked -> updateSettings(upload = isChecked) }

            //Установка обработчика на кнопку сохранения настроек
            btnSave.setOnClickListener { updateAndSaveSettings() }
        }

        //Подписываемся на изменение настроек в viewModel и изменить их в интерфейсе
        viewModel.settings.observe(viewLifecycleOwner) { settings ->
            binding.apply {

                //Устанавливаем тему
                when (settings.theme) {
                    Theme.DARK -> rbDark.isChecked = true
                    Theme.LIGHT -> rbLight.isChecked = true
                    Theme.SYSTEM -> rbSystem.isChecked = true
                }
                //Устанавливаем URL для загрузки и скачивания данных
                etServerDownloadUrl.setText(settings.downloadUrl)
                etServerUploadUrl.setText(settings.uploadUrl)

                //Устанавливаем чекбоксы режима тестирования
                cbDownload.isChecked = settings.download
                cbUpload.isChecked = settings.upload
            }
        }
    }

    //Обновляем состояние настроек в viewModel
    private fun updateSettings(
        theme: Theme = viewModel.settings.value?.theme ?: Theme.SYSTEM,
        downloadUrl: String = binding.etServerDownloadUrl.text.toString(),
        uploadUrl: String = binding.etServerUploadUrl.text.toString(),
        download: Boolean = binding.cbDownload.isChecked,
        upload: Boolean = binding.cbUpload.isChecked
    ) {
        viewModel.updateSettings(theme, downloadUrl, uploadUrl, download, upload)
    }

    //Обновляем и сохраняем настройки
    private fun updateAndSaveSettings() {
        updateSettings()
        viewModel.saveSettings()
        restartActivity()
    }

    //Перезапускаем текущую активити для применения настроек
    private fun restartActivity() {
        val intent = requireActivity().intent
        requireActivity().finish()
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //Освобождаем binding, чтобы избежать утечки памяти
        _binding = null
    }
}