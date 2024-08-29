package com.arkadii.myspeedtest.ui.speedtest

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.arkadii.myspeedtest.domain.service.SpeedTestService
import com.arkadii.myspeedtest.network.SpeedTestServiceImpl
import com.arkadii.myspeedtest.util.SettingsUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SpeedTestViewModel(private val application: Application) : AndroidViewModel(application) {
    private val speedTestService: SpeedTestService = SpeedTestServiceImpl()
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

    @Volatile
    private var isDownloadComplete = true

    @Volatile
    private var isUploadComplete = true

    fun start() {
        val settings = SettingsUtil.loadSettings(application)
        _blockStartButton.value = true
        _clearFields.value = Unit

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                if (settings.download) {
                    isDownloadComplete = false
                    speedTestService.startDownloadSpeedTest(
                        url = settings.downloadUrl,
                        instantSpeed = {
                            _instantDownload.postValue(it.instantSpeedMbps)
                        },
                        averageSpeed = {
                            _averageDownload.postValue(it.instantSpeedMbps)
                            isDownloadComplete = true
                            checkAllTestsComplete()
                        }
                    )
                }

                if (settings.upload) {
                    isUploadComplete = false
                    speedTestService.startUploadSpeedTest(
                        url = settings.uploadUrl,
                        instantSpeed = {
                            _instantUpload.postValue(it.instantSpeedMbps)
                        },
                        averageSpeed = {
                            _averageUpload.postValue(it.instantSpeedMbps)
                            isUploadComplete = true
                            checkAllTestsComplete()
                        }
                    )
                }

                if (!settings.download && !settings.upload) {
                    _blockStartButton.postValue(false)
                    _clearFields.postValue(Unit)
                }
            }
        }
    }

    private fun checkAllTestsComplete() {
        if (isDownloadComplete && isUploadComplete) {
            _blockStartButton.postValue(false)
        }
    }
}