package com.arkadii.myspeedtest.domain.usecases

import com.arkadii.myspeedtest.domain.model.SpeedTestResult
import com.arkadii.myspeedtest.domain.service.SpeedTestService

/**
 * Класс используется для инкапсуляции логики запуска теста скачивания и передачи
 * результатов обратно в presentation через SpeedTestService
 **/
class StartDownloadSpeed(private val service: SpeedTestService) {
    operator fun invoke(
        url: String,
        instantSpeed: (SpeedTestResult) -> Unit,
        averageSpeed: (SpeedTestResult) -> Unit
    ) = service.startDownloadSpeedTest(url, instantSpeed, averageSpeed)
}
