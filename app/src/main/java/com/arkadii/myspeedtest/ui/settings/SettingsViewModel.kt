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
        serverUrl: String = _settings.value?.serverUrl.orEmpty(),
        download: Boolean = _settings.value?.download ?: true,
        upload: Boolean = _settings.value?.upload ?: true
    ) {
        _settings.value = Settings(theme, serverUrl, download, upload)
    }

    fun saveSettings() {
        _settings.value?.let { settings ->
            SettingsUtil.saveSettings(
                getApplication(),
                settings.theme,
                settings.serverUrl.ifEmpty { SettingsUtil.DEFAULT_SERVER_URL },
                settings.download,
                settings.upload
            )
        }
    }

    private fun loadSettings() {
        _settings.value = SettingsUtil.loadSettings(getApplication())
    }
}