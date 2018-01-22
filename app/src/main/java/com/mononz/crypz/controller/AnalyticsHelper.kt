package com.mononz.crypz.controller

import android.app.Application
import android.os.Bundle
import com.crashlytics.android.Crashlytics
import com.google.firebase.analytics.FirebaseAnalytics
import com.mononz.crypz.data.remote.model.ErrorCommon
import io.fabric.sdk.android.Fabric
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnalyticsHelper @Inject constructor(context: Application) {

    private val firebase: FirebaseAnalytics

    init {
        this.firebase = FirebaseAnalytics.getInstance(context)
        this.firebase.setAnalyticsCollectionEnabled(true)

        Fabric.with(context, Crashlytics())
    }


    // Track Screens

    fun trackScreen(screen: String) {
        val bundle = Bundle()
        bundle.putString("screen_name", screen)
        firebase.logEvent("screen", bundle)
    }


    // Errors

    fun trackError(error: ErrorCommon?) {
        Crashlytics.log(error?.message)
    }

    fun trackException(e: Exception) {
        Crashlytics.logException(e)
    }

    companion object {

        val SCREEN_MAIN = "main"
    }

}