package com.arkadii.myspeedtest.util

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

/**
 * Утилита для загрузки и сохранения настроек в @SharedPreferences
 */
object SettingsUtil {
    //Название для фаила
    private const val PREFS_NAME = "AppSettings"

    //Ключи для сохранения и загрузки данных
    private const val KEY_SELECTED_THEME = "selectedTheme"
    private const val KEY_DOWNLOAD_URL = "downloadUrl"
    private const val KEY_UPLOAD_URL = "uploadUrl"
    private const val KEY_DOWNLOAD = "download"
    private const val KEY_UPLOAD = "upload"

    //Значения для настроек по умолчанию
    private val DEFAULT_SELECTED_THEME = Theme.SYSTEM.themeId
    const val DEFAULT_DOWNLOAD_URL = "https://my-server-heu4.onrender.com/download?size=10"
    const val DEFAULT_UPLOAD_URL = "https://my-server-heu4.onrender.com/upload"
    private const val DEFAULT_DOWNLOAD = true
    private const val DEFAULT_UPLOAD = true

    //Метод для получения SharedPreferences
    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    //Метод для сохранения настроек
    fun saveSettings(
        context: Context,
        theme: Theme,
        downloadUrl: String,
        uploadUrl: String,
        download: Boolean,
        upload: Boolean
    ) {
        val sharedPreferences = getSharedPreferences(context)
        val editor = sharedPreferences.edit()

        //Редактируем настройки
        editor.putInt(KEY_SELECTED_THEME, theme.themeId)
        editor.putString(KEY_DOWNLOAD_URL, downloadUrl)
        editor.putString(KEY_UPLOAD_URL, uploadUrl)
        editor.putBoolean(KEY_DOWNLOAD, download)
        editor.putBoolean(KEY_UPLOAD, upload)
        editor.apply()
    }

   //Метод для загрузки настроек
    fun loadSettings(context: Context): Settings {
        //Читаем данные и устанавливаем настройки по умолчанию в случае их отсутствия
        val sharedPreferences = getSharedPreferences(context)
        val themeId =
            sharedPreferences.getInt(KEY_SELECTED_THEME, DEFAULT_SELECTED_THEME)
        val downloadUrl =
            sharedPreferences.getString(KEY_DOWNLOAD_URL, DEFAULT_DOWNLOAD_URL)
                ?: DEFAULT_DOWNLOAD_URL
        val uploadUrl =
            sharedPreferences.getString(KEY_UPLOAD_URL, DEFAULT_UPLOAD_URL) ?: DEFAULT_UPLOAD_URL
        val download =
            sharedPreferences.getBoolean(KEY_DOWNLOAD, DEFAULT_DOWNLOAD)
        val upload = sharedPreferences.getBoolean(KEY_UPLOAD, DEFAULT_UPLOAD)

        return Settings(Theme.getThemeById(themeId), downloadUrl, uploadUrl, download, upload)
    }
}

//Перечесление всех видов тем
enum class Theme(val themeId: Int) {
    //Устанавливаем id каждой темы
    DARK(AppCompatDelegate.MODE_NIGHT_YES),
    LIGHT(AppCompatDelegate.MODE_NIGHT_NO),
    SYSTEM(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

    companion object {
        //Ищем тему по id
        fun getThemeById(id: Int): Theme {
            return entries.find { it.themeId == id } ?: SYSTEM
        }
    }
}

//Класс данных предоставляющий настройки приложения
data class Settings(
    val theme: Theme,
    val downloadUrl: String,
    val uploadUrl: String,
    val download: Boolean,
    val upload: Boolean
)