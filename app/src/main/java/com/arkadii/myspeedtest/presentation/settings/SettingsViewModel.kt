package com.arkadii.myspeedtest.presentation.settings

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arkadii.myspeedtest.util.Settings
import com.arkadii.myspeedtest.util.SettingsUtil
import com.arkadii.myspeedtest.util.Theme
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(private val application: Application) : ViewModel() {
    //Лайв дата оповещает интерфейс об изменении настроек
    private val _settings = MutableLiveData<Settings>()
    val settings: LiveData<Settings> get() = _settings

   //Инициализация viewModel загружает настройки и оповещает интерфейс
    init {
        loadSettings()
    }

    //Обновляет текущие состояние настроек
    fun updateSettings(
        theme: Theme,
        downloadUrl: String = _settings.value?.downloadUrl.orEmpty(),
        uploadUrl: String = _settings.value?.uploadUrl.orEmpty(),
        download: Boolean = _settings.value?.download ?: true,
        upload: Boolean = _settings.value?.upload ?: true
    ) {
        _settings.value = Settings(theme, downloadUrl, uploadUrl, download, upload)
    }

    //Сохраняет текущие настройки в SharedPreference используя утилитарный объект
    fun saveSettings() {
        _settings.value?.let { settings ->
            SettingsUtil.saveSettings(
                application,
                settings.theme,
                settings.downloadUrl.ifEmpty { SettingsUtil.DEFAULT_DOWNLOAD_URL },
                settings.uploadUrl.ifEmpty { SettingsUtil.DEFAULT_UPLOAD_URL },
                settings.download,
                settings.upload
            )
        }
    }

    //Загружает сохранненые настройки из SharedPreference с помощью утилитарного объекта и оповещает интерфейс
    private fun loadSettings() {
        _settings.value = SettingsUtil.loadSettings(application)
    }
}