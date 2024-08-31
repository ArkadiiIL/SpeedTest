package com.arkadii.myspeedtest.presentation.speedtest

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arkadii.myspeedtest.domain.service.SpeedTestService
import com.arkadii.myspeedtest.util.SettingsUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

//Используем Dagger для внедрения зависимостей
@HiltViewModel
class SpeedTestViewModel @Inject constructor(
    private val application: Application,
    private val speedTestService: SpeedTestService
) : ViewModel() {
    //Лайв даты для оповещения об изменениях интерфейс
    private val _instantDownload = MutableLiveData<String>()
    val instantDownload: LiveData<String> = _instantDownload
    private val _averageDownload = MutableLiveData<String>()
    val averageDownload: LiveData<String> = _averageDownload
    private val _instantUpload = MutableLiveData<String>()
    val instantUpload: LiveData<String> = _instantUpload
    private val _averageUpload = MutableLiveData<String>()
    val averageUpload = _averageUpload
    private val _blockStartButton = MutableLiveData<Boolean>()
    val blockStartButton: LiveData<Boolean> = _blockStartButton
    private val _clearFields = MutableLiveData<Unit>()
    val clearFields: LiveData<Unit> = _clearFields
    private val _showError = MutableLiveData<String?>()
    val showError = _showError

    //Флаги указывающие на завершение тестов загрузки выгрузки с аннотацией volatile для корректного многопоточного доступа
    @Volatile
    private var isDownloadComplete = true

    @Volatile
    private var isUploadComplete = true

    //Метод запускающий тесто скорости
    fun start() {
        //Загружаем текущие настройки
        val settings = SettingsUtil.loadSettings(application)
        //Блокируем кнопку старта
        _blockStartButton.value = true
        //Очищаем поля
        _clearFields.value = Unit

        //Запускаем выполнение в отдельном потоке
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                //Если включен тест скачивания запускаем его
                if (settings.download) {
                    isDownloadComplete = false
                    speedTestService.startDownloadSpeedTest(
                        url = settings.downloadUrl,
                        instantSpeed = { result ->
                            //Проверяем возникла ли ошибка
                            if (result.isError) {
                                //Показываем текст ошибки
                                _showError.postValue(result.errorText)
                            } else {
                                //Устанавливаем значение
                                _instantDownload.postValue(
                                    formatResultText(result.instantSpeedMbps)
                                )
                            }
                        },
                        averageSpeed = { result ->
                            //Проверяем возникла ли ошибка
                            if (result.isError) {
                                //Показываем текст ошибки
                                _showError.postValue(result.errorText)
                            } else {
                                //Устанавливаем значение
                                _averageDownload.postValue(
                                    formatResultText(result.instantSpeedMbps)
                                )
                            }
                            //Устанавливаем флаг о завершении теста
                            isDownloadComplete = true
                            //Проверка все ли тесты завершены
                            checkAllTestsComplete()
                        }
                    )
                }

                //Если включен тест загрузки запускаем его
                if (settings.upload) {
                    isUploadComplete = false
                    speedTestService.startUploadSpeedTest(
                        url = settings.uploadUrl,
                        instantSpeed = { result ->
                            if (result.isError) {
                                _showError.postValue(result.errorText)
                            } else {
                                _instantUpload.postValue(
                                    formatResultText(result.instantSpeedMbps)
                                )
                            }
                        },
                        averageSpeed = { result ->
                            if (result.isError) {
                                _showError.postValue(result.errorText)
                            } else {
                                _averageUpload.postValue(
                                    formatResultText(result.instantSpeedMbps)
                                )
                            }
                            isUploadComplete = true
                            checkAllTestsComplete()
                        }
                    )
                }

                //Если оба теста не включены, отменяем тест, разблокируем кнопку и очищаем поля
                if (!settings.download && !settings.upload) {
                    _blockStartButton.postValue(false)
                    _clearFields.postValue(Unit)
                }
            }
        }
    }

    //Метод проверяем завершены ли оба тесты и в этом случае разблокирует кнопку старта для нового теста
    private fun checkAllTestsComplete() {
        if (isDownloadComplete && isUploadComplete) {
            _blockStartButton.postValue(false)
        }
    }

    //Форматирует результат теста
    private fun formatResultText(result: String?): String {
        return if (result != null) {
            "$result $MBPS_POSTFIX"
        } else "0 $MBPS_POSTFIX"
    }

    companion object {
        private const val MBPS_POSTFIX = "Mb/s"
    }
}