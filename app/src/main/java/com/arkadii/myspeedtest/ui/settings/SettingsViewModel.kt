package com.arkadii.myspeedtest.ui.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.arkadii.myspeedtest.util.Settings
import com.arkadii.myspeedtest.util.SettingsUtil
import com.arkadii.myspeedtest.util.Theme

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val _settings = MutableLiveData<Settings>()
    val settings: LiveData<Settings> get() = _settings

    init {
        loadSettings()
    }

    fun updateSettings(
        theme: Theme,
        downloadUrl: String = _settings.value?.downloadUrl.orEmpty(),
        uploadUrl: String = _settings.value?.uploadUrl.orEmpty(),
        download: Boolean = _settings.value?.download ?: true,
        upload: Boolean = _settings.value?.upload ?: true
    ) {
        _settings.value = Settings(theme, downloadUrl, uploadUrl, download, upload)
    }

    fun saveSettings() {
        _settings.value?.let { settings ->
            SettingsUtil.saveSettings(
                getApplication(),
                settings.theme,
                settings.downloadUrl.ifEmpty { SettingsUtil.DEFAULT_DOWNLOAD_URL },
                settings.uploadUrl.ifEmpty { SettingsUtil.DEFAULT_UPLOAD_URL },
                settings.download,
                settings.upload
            )
        }
    }

    private fun loadSettings() {
        _settings.value = SettingsUtil.loadSettings(getApplication())
    }
}