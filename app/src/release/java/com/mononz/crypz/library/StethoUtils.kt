package com.mononz.crypz.library

import android.app.Application

import okhttp3.OkHttpClient

/**
 * Stetho utils class
 */
object StethoUtils {

    /**
     * Application context
     * @param application Application
     */
    fun install(application: Application) {
        // Do nothing. We don't want stetho on release builds
    }

    /**
     * Install interceptors
     * @return OkHttpClient.Builder
     */
    fun installInterceptors(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        // Do nothing. We don't want install any network inspectors on release builds
        return builder
    }
}