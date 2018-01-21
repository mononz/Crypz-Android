package com.mononz.crypz.base

import android.app.Activity
import android.app.Application
import com.mononz.crypz.base.Constants.HEADER_JSON

import com.mononz.crypz.controller.PreferenceHelper
import com.mononz.crypz.data.local.CrypzDatabase
import com.mononz.crypz.data.local.entity.CoinEntity
import com.mononz.crypz.data.local.entity.MarketCoinEntity
import com.mononz.crypz.data.local.entity.MarketEntity
import com.mononz.crypz.data.remote.NetworkInterface
import com.mononz.crypz.injection.component.DaggerAppComponent
import com.mononz.crypz.library.StethoUtils

import org.json.JSONObject

import java.util.ArrayList
import java.util.concurrent.Callable

import javax.inject.Inject

import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.RequestBody
import timber.log.Timber

class Crypz : Application(), HasActivityInjector {

    @Inject lateinit var activityDispatchingInjector: DispatchingAndroidInjector<Activity>

    @Inject lateinit var network: NetworkInterface
    @Inject lateinit var database: CrypzDatabase
    @Inject lateinit var session: PreferenceHelper

    private val disposables = CompositeDisposable()

    override fun onCreate() {
        super.onCreate()

        StethoUtils.install(this)

        DaggerAppComponent.builder()
                .application(this)
                .build()
                .inject(this)

        val thresholdMillis = 1 * 60 * 1000  // 1 mins
        if (System.currentTimeMillis() > session.lastUpdated + thresholdMillis) {
            sync()
        }
    }

    override fun activityInjector(): AndroidInjector<Activity>? {
        return activityDispatchingInjector
    }

    private fun sync() {
        Timber.d("onPerformSync")

        val json = JSONObject()
        try {
            json.put(CoinEntity.TABLE_NAME, lastUpdatedAt(CoinEntity.TABLE_NAME))
            json.put(MarketEntity.TABLE_NAME, lastUpdatedAt(MarketEntity.TABLE_NAME))
            json.put(MarketCoinEntity.TABLE_NAME, lastUpdatedAt(MarketCoinEntity.TABLE_NAME))
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val d = network.sync(HEADER_JSON, RequestBody.create(MediaType.parse(HEADER_JSON), json.toString()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy (
                        onNext = {
                            val coin = ArrayList<CoinEntity>()
                            val market = ArrayList<MarketEntity>()
                            val marketCoin = ArrayList<MarketCoinEntity>()

                            it.data!!.coin!!.mapTo(coin) { CoinEntity.createEntity(it) }
                            it.data!!.market!!.mapTo(market) { MarketEntity.createEntity(it) }
                            it.data!!.market_coin!!.mapTo(marketCoin) { MarketCoinEntity.createEntity(it) }

                            asyncSave(Callable{
                                database.coinDao().insert(coin)
                                database.marketDao().insert(market)
                                database.marketCoinDao().insert(marketCoin)
                                true
                            })
                        },
                        onError =  {
                            it.printStackTrace()
                        },
                        onComplete = {
                            session.lastUpdated = System.currentTimeMillis()
                            Timber.d("Sync Complete")
                        }
                )
        disposables.add(d)
    }

    private fun lastUpdatedAt(tableName: String): String {
        var value = "0"
        val c = database.query("SELECT updated_at FROM $tableName ORDER BY updated_at DESC LIMIT 1", null)
        if (c != null) {
            if (c.moveToFirst()) {
                value = c.getString(0)
            }
            c.close()
        }
        return value
    }

    companion object {

        fun asyncSave(callable: Callable<*>) {
            Observable.fromCallable(callable)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeBy (
                            onNext = { println(it) },
                            onError =  { it.printStackTrace() },
                            onComplete = { println("Done!") }
                    )}
    }

}