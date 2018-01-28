package com.mononz.crypz.data.remote

import com.mononz.crypz.data.remote.model.MsStake
import com.mononz.crypz.data.remote.model.MsSync
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface NetworkInterface {

    @POST("sync") fun sync(
            @Header("Content-Type") contentType: String,
            @Body json: RequestBody)
            : Observable<MsSync>

    @POST("stakes") fun renewStakePrices(
            @Header("Content-Type") contentType: String,
            @Body json: RequestBody)
            : Observable<List<MsStake>>

}