package com.mononz.crypz.viewmodel

import androidx.lifecycle.ViewModel

import com.mononz.crypz.data.local.CrypzDatabase

import javax.inject.Inject

class GenericViewModel @Inject constructor() : ViewModel() {

    @Inject lateinit var database: CrypzDatabase

}