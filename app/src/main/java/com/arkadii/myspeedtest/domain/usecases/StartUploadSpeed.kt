package com.arkadii.myspeedtest.domain.usecases

import com.arkadii.myspeedtest.domain.model.SpeedTestResult
import com.arkadii.myspeedtest.domain.service.SpeedTestService

/**
 * Класс используется для инкапсуляции логики запуска теста загрузки и передачи
 * результатов обратно в presentation через SpeedTestService
 **/
class StartUploadSpeed(private val service: SpeedTestService) {
    operator fun invoke(
        url: String,
        instantSpeed: (SpeedTestResult) -> Unit,
        averageSpeed: (SpeedTestResult) -> Unit
    ) = service.startUploadSpeedTest(url, instantSpeed, averageSpeed)
}
