package com.arkadii.myspeedtest.domain.service

import com.arkadii.myspeedtest.domain.model.SpeedTestResult

interface SpeedTestService {
    fun startDownloadSpeedTest(
        url: String,
        instantSpeed: (SpeedTestResult) -> Unit,
        averageSpeed: (SpeedTestResult) -> Unit
    )

    fun startUploadSpeedTest(
        url: String,
        instantSpeed: (SpeedTestResult) -> Unit,
        averageSpeed: (SpeedTestResult) -> Unit
    )
}