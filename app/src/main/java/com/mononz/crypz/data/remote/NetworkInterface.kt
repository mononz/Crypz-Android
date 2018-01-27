package com.mononz.crypz.data.remote

import com.mononz.crypz.data.remote.model.MsPrices
import com.mononz.crypz.data.remote.model.MsSync

import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.*

interface NetworkInterface {

    @POST("sync")
    fun sync(@Header("Content-Type") contentType: String,
             @Body json: RequestBody)
            : Observable<MsSync>

    @POST("prices/app")
    fun prices(@Header("Content-Type") contentType: String,
               @Body json: RequestBody)
            : Observable<MsPrices>

}