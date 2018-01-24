package com.mononz.crypz.data.remote

import com.mononz.crypz.BuildConfig
import com.mononz.crypz.controller.PreferenceHelper
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class RequestInterceptor(private val session: PreferenceHelper) : okhttp3.Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var originalRequest = chain.request()
        val builder = originalRequest.newBuilder()
        builder.addHeader("Authorization", BuildConfig.BASIC_AUTH)
        builder.addHeader("Platform", "Android")
        builder.addHeader("Version", BuildConfig.VERSION_NAME)
        builder.addHeader("VersionCode", BuildConfig.VERSION_CODE.toString())
        originalRequest = builder.build()

        return chain.proceed(originalRequest)
    }

}