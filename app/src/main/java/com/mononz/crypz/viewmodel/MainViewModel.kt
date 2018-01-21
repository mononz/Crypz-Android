package com.mononz.crypz.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.mononz.crypz.data.Repository

import com.mononz.crypz.data.local.custom.StakeSummary

import javax.inject.Inject

class MainViewModel @Inject constructor() : ViewModel() {

    @Inject lateinit var repository: Repository

    fun getCoins(): LiveData<List<StakeSummary>> {
        return repository.getCoins()
    }
}