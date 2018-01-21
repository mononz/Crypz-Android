package com.mononz.crypz.controller

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferenceHelper @Inject constructor(context: Application) {

    val sharedPreferences: SharedPreferences


    // LastUpdated

    var lastUpdated: Long
        get() = sharedPreferences.getLong(LastUpdated, 0)
        set(timestamp) {
            val editor = sharedPreferences.edit()
            editor.putLong(LastUpdated, timestamp)
            editor.apply()
        }

    init {
        sharedPreferences = context.getSharedPreferences(Preferences, Context.MODE_PRIVATE)
    }

    companion object {

        private val Preferences = "Preferences"

        private val LastUpdated = "last_updated"
    }

}