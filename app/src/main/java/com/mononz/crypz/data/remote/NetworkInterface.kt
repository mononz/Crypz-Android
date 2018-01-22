package com.mononz.crypz.data.remote

import com.mononz.crypz.data.remote.model.MsSync

import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.*

interface NetworkInterface {

    @POST("sync")
    fun sync(
            @Header("Content-Type") contentType: String,
            @Body json: RequestBody)
            : Observable<MsSync>

    @FormUrlEncoded
    @POST("prices")
    fun prices(
            @Field("market_coin_ids") marketCoinIds: String)
            : Observable<String>

}