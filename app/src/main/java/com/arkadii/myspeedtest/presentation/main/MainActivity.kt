package com.arkadii.myspeedtest.presentation.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.arkadii.myspeedtest.R
import com.arkadii.myspeedtest.databinding.ActivityMainBinding
import com.arkadii.myspeedtest.util.SettingsUtil
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        //Прменяет сохраненную тему.
        applySavedTheme()
        super.onCreate(savedInstanceState)

        //Инициализация binding который будет использовать для вызова элементов интерфейса.
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        //Получаем navController для управления навигацией.
        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        //Установка фрагментов.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_speed_test, R.id.navigation_settings
            )
        )
        //Настройка ActionBar чтобы синхранизировать заголовко и стрелку назад.
        setupActionBarWithNavController(navController, appBarConfiguration)
        //Настройка BottomNavigationView чтобы менять фрагменты.
        navView.setupWithNavController(navController)
    }

    /**
     * Метод для применения сохранённой темы приложения.
     * Загружает настройки, где хранится информация о выбранной теме.
     */
    private fun applySavedTheme() {
        val settings = SettingsUtil.loadSettings(this)
        AppCompatDelegate.setDefaultNightMode(settings.theme.themeId)
    }
}