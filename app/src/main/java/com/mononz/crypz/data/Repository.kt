package com.mononz.crypz.data

import com.mononz.crypz.data.local.CrypzDatabase
import com.mononz.crypz.data.remote.NetworkInterface

import javax.inject.Inject

class Repository @Inject constructor() {

    @Inject lateinit var network: NetworkInterface
    @Inject lateinit var database: CrypzDatabase

}
