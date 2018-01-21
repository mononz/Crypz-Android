package com.mononz.crypz.controller

import com.mononz.crypz.data.remote.model.ErrorCommon
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException

import javax.inject.Inject
import javax.inject.Singleton

import retrofit2.HttpException
import retrofit2.Retrofit

@Singleton
class ErrorHelper @Inject constructor(private val analytics: AnalyticsHelper, private val retrofit: Retrofit) {

    fun parseError(error: Throwable): ErrorCommon {
        val err = parseError2(error)
        analytics.trackError(err)
        return err
    }

    private fun parseError2(error: Throwable): ErrorCommon {
        if (error is HttpException) {
            val response = error.response()
            if (response != null) {

                // Catch main status code errors first
                when (response.code()) {
                    504 -> return ErrorCommon("Timeout")
                    503 -> return ErrorCommon("Maintenance")
                    401 -> return ErrorCommon("Unauthorized")
                }
                if (response.code() >= 500) return ErrorCommon("Server Error")

                try {
                    // Try parse response body
                    val errorConverter = retrofit.responseBodyConverter<ErrorCommon>(ErrorCommon::class.java, arrayOfNulls(0))
                    val errorBody = response.errorBody()
                    if (errorBody != null) {
                        val err = errorConverter.convert(errorBody)
                        if (err != null) return err
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    analytics.trackException(e)
                }

            }
        }
        return fallbackError(error)
    }

    private fun fallbackError(error: Throwable): ErrorCommon {
        var e = ErrorCommon("Unknown Error")
        if (error is IllegalStateException) {
            e = ErrorCommon("Conversion Error")
        } else if (error is ConnectException) {
            e = ErrorCommon("Failed to connect to server")
        } else if (error is SocketTimeoutException) {
            e = ErrorCommon("Timeout")
        } else if (error is SocketException) {
            e = ErrorCommon("Server Currently Unavailable")
        }
        return e
    }
}