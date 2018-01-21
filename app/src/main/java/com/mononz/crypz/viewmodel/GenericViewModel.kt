package com.mononz.crypz.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel

import com.mononz.crypz.data.local.CrypzDatabase
import com.mononz.crypz.data.local.entity.CoinEntity

import javax.inject.Inject

class GenericViewModel @Inject constructor() : ViewModel() {

    @Inject lateinit var database: CrypzDatabase

    val coins: LiveData<CoinEntity>
        get() = database.coinDao().query()
}