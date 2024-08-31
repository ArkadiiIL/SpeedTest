package com.arkadii.myspeedtest

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * SpeedTestApp основной класс приложения
 * @HiltAndroidApp аннотация Hilt, указывает, что данный класс будет корнем графа зависимостей
 */
@HiltAndroidApp
class SpeedTestApp : Application()