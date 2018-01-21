package com.mononz.crypz.controller

import android.app.Application
import android.content.Context
import android.widget.ImageView

import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

import javax.inject.Singleton

@Singleton
class ImageHelper(context: Application) {

    private val context: Context

    init {
        this.context = context
    }

    fun load(view: ImageView, path: String) {
        Glide.with(context)
                .load(path)
                .apply(OPTIONS)
                .into(view)
    }

    companion object {

        val OPTIONS = RequestOptions()
                .placeholder(null)
                .error(null)
    }

}