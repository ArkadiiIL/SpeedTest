package com.arkadii.myspeedtest.domain.model

//Модель данных которая передается между presentation и network
data class SpeedTestResult(
    val isError: Boolean,
    val instantSpeedMbps: String?,
    val errorText: String?
)