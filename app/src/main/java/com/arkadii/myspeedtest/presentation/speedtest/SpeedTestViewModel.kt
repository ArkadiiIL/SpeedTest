package com.arkadii.myspeedtest.presentation.speedtest

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arkadii.myspeedtest.R
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
    private val _showError = MutableLiveData<String?>()
    val showError = _showError

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
                            if (result.isError) {
                                _showError.postValue(result.errorText)
                            } else {
                                _instantDownload.postValue(
                                    formatResultText(result.instantSpeedMbps)
                                )
                            }
                        },
                        averageSpeed = { result ->
                            if (result.isError) {
                                _showError.postValue(result.errorText)
                            } else {
                                _averageDownload.postValue(
                                    formatResultText(result.instantSpeedMbps)
                                )
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

    private fun formatResultText(result: String?): String {
        return if (result != null) {
            "$result $MBPS_POSTFIX"
        } else "0 $MBPS_POSTFIX"
    }

    companion object {
        private const val MBPS_POSTFIX = "Mb/s"
    }
}