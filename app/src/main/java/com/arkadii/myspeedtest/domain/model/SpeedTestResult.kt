package com.arkadii.myspeedtest.domain.model

data class SpeedTestResult(
    val isError: Boolean,
    val instantSpeedMbps: String?,
    val errorText: String?
)