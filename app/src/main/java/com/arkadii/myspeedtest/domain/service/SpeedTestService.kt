package com.arkadii.myspeedtest.domain.service

import com.arkadii.myspeedtest.domain.model.SpeedTestResult

//Интерфейс определяющий основной функционал для взаимодействия presentation и network
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