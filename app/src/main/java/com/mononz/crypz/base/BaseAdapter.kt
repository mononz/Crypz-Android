package com.mononz.crypz.base

import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<Type : androidx.recyclerview.widget.RecyclerView.ViewHolder, in Data, in Callback> : androidx.recyclerview.widget.RecyclerView.Adapter<Type>() {

    abstract fun setData(data: List<Data>)

    abstract fun setCallback(callback: Callback)

    companion object {

        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }
}
