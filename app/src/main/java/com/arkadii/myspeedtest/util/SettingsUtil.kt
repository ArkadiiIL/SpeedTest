package com.arkadii.myspeedtest.util

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

object SettingsUtil {
    private const val PREFS_NAME = "AppSettings"

    private const val KEY_SELECTED_THEME = "selectedTheme"
    private const val KEY_SERVER_URL = "serverUrl"
    private const val KEY_DOWNLOAD = "download"
    private const val KEY_UPLOAD = "upload"

    private val DEFAULT_SELECTED_THEME = Theme.SYSTEM.themeId
    const val DEFAULT_SERVER_URL = "http://2.testdebit.info/fichiers/1Mo.dat"
    private const val DEFAULT_DOWNLOAD = true
    private const val DEFAULT_UPLOAD = true

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun saveSettings(
        context: Context,
        theme: Theme,
        serverUrl: String,
        download: Boolean,
        upload: Boolean
    ) {
        val sharedPreferences = getSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.putInt(KEY_SELECTED_THEME, theme.themeId)
        editor.putString(KEY_SERVER_URL, serverUrl)
        editor.putBoolean(KEY_DOWNLOAD, download)
        editor.putBoolean(KEY_UPLOAD, upload)
        editor.apply()
    }

    fun loadSettings(context: Context): Settings {
        val sharedPreferences = getSharedPreferences(context)
        val themeId =
            sharedPreferences.getInt(KEY_SELECTED_THEME, DEFAULT_SELECTED_THEME)
        val serverUrl =
            sharedPreferences.getString(KEY_SERVER_URL, DEFAULT_SERVER_URL) ?: DEFAULT_SERVER_URL
        val download =
            sharedPreferences.getBoolean(KEY_DOWNLOAD, DEFAULT_DOWNLOAD)
        val upload = sharedPreferences.getBoolean(KEY_UPLOAD, DEFAULT_UPLOAD)

        return Settings(Theme.getThemeById(themeId), serverUrl, download, upload)
    }
}

enum class Theme(val themeId: Int) {
    DARK(AppCompatDelegate.MODE_NIGHT_YES),
    LIGHT(AppCompatDelegate.MODE_NIGHT_NO),
    SYSTEM(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

    companion object {
        fun getThemeById(id: Int): Theme {
            return entries.find { it.themeId == id } ?: SYSTEM
        }
    }
}

data class Settings(
    val theme: Theme,
    val serverUrl: String,
    val download: Boolean,
    val upload: Boolean
)