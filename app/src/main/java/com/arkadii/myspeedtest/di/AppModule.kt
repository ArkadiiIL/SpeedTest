package com.arkadii.myspeedtest.di

import android.content.Context
import com.arkadii.myspeedtest.domain.service.SpeedTestService
import com.arkadii.myspeedtest.domain.usecases.ServiceUseCases
import com.arkadii.myspeedtest.domain.usecases.StartDownloadSpeed
import com.arkadii.myspeedtest.domain.usecases.StartUploadSpeed
import com.arkadii.myspeedtest.network.SpeedTestServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

//Модуль Dagger предоставляющий зависимости для всего приложения
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideSpeedTestService(@ApplicationContext context: Context): SpeedTestService =
        SpeedTestServiceImpl(context)

    @Provides
    @Singleton
    fun provideServiceUseCases(service: SpeedTestService) = ServiceUseCases(
        startDownloadSpeedTestUseCase = StartDownloadSpeed(service),
        startUploadSpeedTestUseCase = StartUploadSpeed(service)
    )
}