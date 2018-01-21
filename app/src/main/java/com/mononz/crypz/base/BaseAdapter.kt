package com.mononz.crypz.base

import android.support.v7.app.AppCompatDelegate
import android.support.v7.widget.RecyclerView

abstract class BaseAdapter<Type : RecyclerView.ViewHolder, in Data, in Callback> : RecyclerView.Adapter<Type>() {

    abstract fun setData(data: List<Data>)

    abstract fun setCallback(callback: Callback)

    companion object {

        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }
}
