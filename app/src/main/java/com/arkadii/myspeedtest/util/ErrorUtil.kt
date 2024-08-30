package com.arkadii.myspeedtest.util

import android.content.Context
import com.arkadii.myspeedtest.R
import fr.bmartel.speedtest.model.SpeedTestError

object ErrorUtil {
    fun getErrorText(context: Context, error: SpeedTestError?): String {
        return when(error) {
            SpeedTestError.INVALID_HTTP_RESPONSE -> context.getString(R.string.invalidResponseError)
            SpeedTestError.SOCKET_ERROR -> context.getString(R.string.socketError)
            SpeedTestError.SOCKET_TIMEOUT -> context.getString(R.string.socketTimeoutError)
            SpeedTestError.CONNECTION_ERROR -> context.getString(R.string.connectionError)
            SpeedTestError.MALFORMED_URI -> context.getString(R.string.malformedUriError)
            SpeedTestError.UNSUPPORTED_PROTOCOL -> context.getString(R.string.usupportedProtocolError)
            null -> context.getString(R.string.unknownError)
        }
    }
}