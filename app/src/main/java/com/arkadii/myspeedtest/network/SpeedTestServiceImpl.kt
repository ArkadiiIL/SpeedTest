package com.arkadii.myspeedtest.network

import com.arkadii.myspeedtest.domain.model.SpeedTestResult
import com.arkadii.myspeedtest.domain.service.SpeedTestService
import com.arkadii.myspeedtest.util.SpeedUtils
import fr.bmartel.speedtest.SpeedTestReport
import fr.bmartel.speedtest.SpeedTestSocket
import fr.bmartel.speedtest.inter.IRepeatListener
import java.math.BigDecimal

class SpeedTestServiceImpl : SpeedTestService {

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

        val allTests = mutableListOf<BigDecimal>()

        val repeatListener = object : IRepeatListener {
            override fun onCompletion(report: SpeedTestReport?) {
                if (report != null) {
                    val averageBits = SpeedUtils.calculateAverageSpeed(allTests)
                    val averageMbps = SpeedUtils.bitsToMbps(averageBits)
                    averageSpeed(SpeedTestResult(averageMbps.toString()))
                } else {
                    averageSpeed(SpeedTestResult("Error"))
                }
            }

            override fun onReport(report: SpeedTestReport?) {
                if (report != null) {
                    allTests.add(report.transferRateBit)
                    val result = SpeedUtils.bitsToMbps(report.transferRateBit)
                    instantSpeed(SpeedTestResult(result.toString()))
                } else {
                    instantSpeed(SpeedTestResult("Error"))
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