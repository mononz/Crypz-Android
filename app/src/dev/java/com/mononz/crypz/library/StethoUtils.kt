package com.mononz.crypz.library

import android.app.Application

import com.facebook.stetho.Stetho
import com.facebook.stetho.okhttp3.StethoInterceptor

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber

/**
 * Stetho utils class
 */
object StethoUtils {

    /**
     * Application context
     * @param application Application
     */
    fun install(application: Application) {
        Stetho.initializeWithDefaults(application)
        Timber.plant(Timber.DebugTree())
    }

    /**
     * Install interceptors
     * @return OkHttpClient.Builder
     */
    fun installInterceptors(builder: OkHttpClient.Builder): OkHttpClient.Builder {

        // Create a logging interceptor for debug builds only
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        // add logging interceptor to client
        builder.addInterceptor(loggingInterceptor)

        // also add stetho interceptor for extra logging with chrome inspector
        builder.addNetworkInterceptor(StethoInterceptor())

        return builder
    }
}