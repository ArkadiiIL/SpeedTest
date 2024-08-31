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

    //Метод запускает тест скорости скачивания
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

    //Метод запускает тест скорости загрузки
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

    //Общий метод для запуска скорости, метод получает два метода которые будут информировать viewModel о полученных результатах
    private fun startSpeedTest(
        url: String,
        isDownload: Boolean,
        instantSpeed: (SpeedTestResult) -> Unit,
        averageSpeed: (SpeedTestResult) -> Unit
    ) {
        //Создаем основной класс библиотеки jspeedtest
        val speedTestSocket = SpeedTestSocket()
        //Добавляем слушатель событий, который будет отслеживать возникающие ошибки
        speedTestSocket.addSpeedTestListener(object : ISpeedTestListener {
            override fun onCompletion(report: SpeedTestReport?) {
            }

            override fun onProgress(percent: Float, report: SpeedTestReport?) {
            }

            override fun onError(speedTestError: SpeedTestError?, errorMessage: String?) {
                //В случае возникновения ошибки получаем текст через утилиту и отправляем результат
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

        //Создаем слушатель событий
        val repeatListener = object : IRepeatListener {
            //Список всех результатов теста
            val allTests = mutableListOf<BigDecimal>()

            //Этот метод вызывается при завершении тестирования
            override fun onCompletion(report: SpeedTestReport?) {
                //Если отчет не является null, вычисляет среднию скорость и отправляет ее, иначе сообщает об ошибке
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

            //Этот метод периодически вызывается для отчета о текущей скорости и добавляем ее в список и отправляет для выовада на экран
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
            //Запускает тест скачивания на определенный url в течении определенного времени(10 секунд) и отправляет отчет каждую(1 секунду)
            speedTestSocket.startDownloadRepeat(
                url,
                10000,
                1000,
                repeatListener
            )
        } else {
            //Запускает тест загрузки на определенный url в течении определенного времени(10 секунд) и отправляет отчет каждую(1 секунду), генерирует фаил размером в (1мб)
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