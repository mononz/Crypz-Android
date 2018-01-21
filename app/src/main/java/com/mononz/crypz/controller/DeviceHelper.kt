package com.mononz.crypz.controller

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviceHelper @Inject constructor(context: Application) {

    private val mContext: Context

    val isNetworkConnected: Boolean
        get() {
            val cm = mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return cm.activeNetworkInfo != null
        }

    init {
        this.mContext = context
    }

}