package com.arkadii.myspeedtest.domain.usecases

//Дата класс для хранения use cases, связанных с тестом скорости
data class ServiceUseCases(
    val startDownloadSpeedTestUseCase: StartDownloadSpeed,
    val startUploadSpeedTestUseCase: StartUploadSpeed
)
