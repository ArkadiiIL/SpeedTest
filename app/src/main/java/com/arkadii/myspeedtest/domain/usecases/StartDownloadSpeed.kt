package com.arkadii.myspeedtest.domain.usecases

import com.arkadii.myspeedtest.domain.model.SpeedTestResult
import com.arkadii.myspeedtest.domain.service.SpeedTestService

class StartDownloadSpeed(private val service: SpeedTestService) {
    operator fun invoke(
        url: String,
        instantSpeed: (SpeedTestResult) -> Unit,
        averageSpeed: (SpeedTestResult) -> Unit
    ) = service.startDownloadSpeedTest(url, instantSpeed, averageSpeed)

}
