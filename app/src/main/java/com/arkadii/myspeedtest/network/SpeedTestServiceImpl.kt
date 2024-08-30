package com.arkadii.myspeedtest.network

import android.content.Context
import com.arkadii.myspeedtest.domain.model.SpeedTestResult
import com.arkadii.myspeedtest.domain.service.SpeedTestService
import com.arkadii.myspeedtest.util.ErrorUtil.getErrorText
import com.arkadii.myspeedtest.util.SpeedUtils
import fr.bmartel.speedtest.SpeedTestReport
import fr.bmartel.speedtest.SpeedTestSocket
import fr.bmartel.speedtest.inter.IRepeatListener
import fr.bmartel.speedtest.inter.ISpeedTestListener
import fr.bmartel.speedtest.model.SpeedTestError
import java.math.BigDecimal

class SpeedTestServiceImpl(private val context: Context) : SpeedTestService {

    override fun startDownloadSpeedTest(
        url: String,
        instantSpeed: (SpeedTestResult) -> Unit,
        averageSpeed: (SpeedTestResult) -> Unit
    ) {
        startSpeedTest(
            url = url,
            isDownload = true,
            instantSpeed = instantSpeed,
            averageSpeed = averageSpeed
        )
    }

    override fun startUploadSpeedTest(
        url: String,
        instantSpeed: (SpeedTestResult) -> Unit,
        averageSpeed: (SpeedTestResult) -> Unit
    ) {
        startSpeedTest(
            url = url,
            isDownload = false,
            instantSpeed = instantSpeed,
            averageSpeed = averageSpeed
        )
    }

    private fun startSpeedTest(
        url: String,
        isDownload: Boolean,
        instantSpeed: (SpeedTestResult) -> Unit,
        averageSpeed: (SpeedTestResult) -> Unit
    ) {
        val speedTestSocket = SpeedTestSocket()
        speedTestSocket.addSpeedTestListener(object : ISpeedTestListener {
            override fun onCompletion(report: SpeedTestReport?) {
            }

            override fun onProgress(percent: Float, report: SpeedTestReport?) {
            }

            override fun onError(speedTestError: SpeedTestError?, errorMessage: String?) {
                instantSpeed(
                    SpeedTestResult(
                        true,
                        null,
                        getErrorText(context, speedTestError)
                    )
                )
                averageSpeed(
                    SpeedTestResult(
                        true,
                        null,
                        getErrorText(context, speedTestError)
                    )
                )
            }
        })

        val repeatListener = object : IRepeatListener {
            val allTests = mutableListOf<BigDecimal>()
            override fun onCompletion(report: SpeedTestReport?) {
                if (report != null) {
                    val averageBits = SpeedUtils.calculateAverageSpeed(allTests)
                    val averageMbps = SpeedUtils.bitsToMbps(averageBits)
                    averageSpeed(
                        SpeedTestResult(false, averageMbps.toString(), null)
                    )
                } else {
                    averageSpeed(
                        SpeedTestResult(
                            true,
                            null,
                            getErrorText(context, null)
                        )
                    )
                }
            }

            override fun onReport(report: SpeedTestReport?) {
                if (report != null) {
                    allTests.add(report.transferRateBit)
                    val result = SpeedUtils.bitsToMbps(report.transferRateBit)
                    instantSpeed(SpeedTestResult(false, result.toString(), null))
                } else {
                    instantSpeed(
                        SpeedTestResult(
                            true,
                            null,
                            "Error"
                        )
                    )
                }
            }
        }

        if (isDownload) {
            speedTestSocket.startDownloadRepeat(
                url,
                10000,
                1000,
                repeatListener
            )
        } else {
            speedTestSocket.startUploadRepeat(
                url,
                10000,
                1000,
                1000000,
                repeatListener
            )
        }
    }
}