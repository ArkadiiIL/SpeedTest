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

@HiltViewModel
class SpeedTestViewModel @Inject constructor(
    private val application: Application,
    private val speedTestService: SpeedTestService
) : ViewModel() {
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
                        instantSpeed = { result ->
                            val speed = result.instantSpeedMbps
                            if (speed != null) {
                                _instantDownload.postValue(speed.toString())
                            }
                        },
                        averageSpeed = { result ->
                            val speed = result.instantSpeedMbps
                            if (speed != null) {
                                _averageDownload.postValue(speed.toString())
                            }
                            isDownloadComplete = true
                            checkAllTestsComplete()
                        }
                    )
                }

                if (settings.upload) {
                    isUploadComplete = false
                    speedTestService.startUploadSpeedTest(
                        url = settings.uploadUrl,
                        instantSpeed = { result ->
                            val speed = result.instantSpeedMbps
                            if (speed != null) {
                                _instantUpload.postValue(speed.toString())
                            }
                        },
                        averageSpeed = { result ->
                            val speed = result.instantSpeedMbps
                            if (speed != null) {
                                _averageUpload.postValue(speed.toString())
                            }
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