package com.arkadii.myspeedtest.util

import java.math.BigDecimal
import java.math.RoundingMode

object SpeedUtils {
    private const val MILLION_NUMBER = "1000000"

    //Переводит биты в секунды в мегабиты в секунду
    fun bitsToMbps(bitsPerSecond: BigDecimal): BigDecimal {
        val megabitsPerSecond =
            bitsPerSecond.divide(BigDecimal(MILLION_NUMBER), RoundingMode.HALF_UP)
        return megabitsPerSecond.setScale(2, RoundingMode.HALF_UP)
    }

    //Метод рассчитывает среднию скорость
    fun calculateAverageSpeed(speeds: List<BigDecimal>): BigDecimal {
        if (speeds.isEmpty()) return BigDecimal.ZERO

        val totalSpeed = speeds.reduce { acc, speed -> acc.add(speed) }
        val averageSpeed = totalSpeed.divide(BigDecimal(speeds.size), RoundingMode.HALF_UP)
        return averageSpeed.setScale(2, RoundingMode.HALF_UP)
    }
}