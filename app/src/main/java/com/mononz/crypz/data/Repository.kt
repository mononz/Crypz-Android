package com.mononz.crypz.data

import android.arch.lifecycle.LiveData
import com.mononz.crypz.base.Constants
import com.mononz.crypz.controller.PreferenceHelper
import com.mononz.crypz.data.local.CrypzDatabase
import com.mononz.crypz.data.local.custom.StakeSummary
import com.mononz.crypz.data.local.entity.*
import com.mononz.crypz.data.remote.NetworkInterface
import com.mononz.crypz.data.remote.model.MsStake
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import timber.log.Timber
import java.util.concurrent.Callable
import javax.inject.Inject

class Repository @Inject constructor() {

    @Inject lateinit var network : NetworkInterface
    @Inject lateinit var database : CrypzDatabase
    @Inject lateinit var session : PreferenceHelper

    private val disposables = CompositeDisposable()

    fun getActiveTrackings() : LiveData<List<StakeSummary>> {
        return database.stakeDao().getActiveTrackings()
    }

    fun getStakesForNetwork() : Single<List<StakeEntity>> {
        return database.stakeDao().getStakesForNetwork()
    }

    fun sync(force : Boolean) {
        val thresholdMillis = 1 * 60 * 1000  // 1 mins
        if (force || System.currentTimeMillis() > session.lastUpdated + thresholdMillis) {
            sync()
        }
    }

    private fun sync() {
        Timber.d("sync")

        val json = JSONObject()
        try {
            json.put(CoinEntity.TABLE_NAME, lastUpdatedAt(CoinEntity.TABLE_NAME))
            json.put(MarketEntity.TABLE_NAME, lastUpdatedAt(MarketEntity.TABLE_NAME))
            json.put(MarketCoinEntity.TABLE_NAME, lastUpdatedAt(MarketCoinEntity.TABLE_NAME))
            json.put(SettingEntity.TABLE_NAME, lastUpdatedAt(SettingEntity.TABLE_NAME))
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val d = network.sync(Constants.HEADER_JSON, RequestBody.create(MediaType.parse(Constants.HEADER_JSON), json.toString()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy (
                        onNext = {
                            val coin = ArrayList<CoinEntity>()
                            val market = ArrayList<MarketEntity>()
                            val marketCoin = ArrayList<MarketCoinEntity>()
                            val setting = ArrayList<SettingEntity>()

                            it.data!!.coin!!.mapTo(coin) { CoinEntity.createEntity(it) }
                            it.data!!.market!!.mapTo(market) { MarketEntity.createEntity(it) }
                            it.data!!.market_coin!!.mapTo(marketCoin) { MarketCoinEntity.createEntity(it) }
                            it.data!!.setting!!.mapTo(setting) { SettingEntity.createEntity(it) }

                            asyncSave(Callable{
                                database.coinDao().insert(coin)
                                database.marketDao().insert(market)
                                database.marketCoinDao().insert(marketCoin)
                                database.settingDao().insert(setting)
                                true
                            })
                        },
                        onError = {
                            it.printStackTrace()
                        },
                        onComplete = {
                            session.lastUpdated = System.currentTimeMillis()
                            Timber.d("Sync Complete")
                        })
        disposables.add(d)
    }

    private fun lastUpdatedAt(tableName : String): String {
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

    private fun asyncSave(callable : Callable<*>) {
        Observable.fromCallable(callable)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy (
                        onNext = { println(it) },
                        onError =  { it.printStackTrace() },
                        onComplete = { println("Done!") }
                )}

    fun insertStake(entity : StakeEntity) : Callable<Unit> {
        return Callable {
            database.stakeDao().insert(entity)
        }
    }

    fun renewStakePrices(entities : List<StakeEntity>) : Observable<List<MsStake>> {
        val stakes = JSONArray()
        entities.forEach({
            stakes.put(StakeEntity.toJson(it))
        })
        return network.renewStakePrices(Constants.HEADER_JSON, RequestBody.create(MediaType.parse(Constants.HEADER_JSON), stakes.toString()))
    }

    fun updateStakes(entities : List<StakeEntity>) {
        asyncSave(Callable {
            database.stakeDao().updateStakes(entities)
        })
    }

    fun deleteStake(entity : StakeEntity) {
        asyncSave(Callable {
            database.stakeDao().deleteStake(entity)
        })
    }

    fun getEnabledMarkets() : Single<List<MarketEntity>> {
        return database.marketDao().getEnabledMarkets()
    }

    fun getEnabledCoins(marketId : Int): Single<List<CoinEntity>> {
        return database.coinDao().getEnabledCoins(marketId)
    }

    fun validateMarketCoinSelection(marketId : Int, coinId : Int): Single<MarketCoinEntity> {
        return database.marketCoinDao().validateMarketCoinSelection(marketId, coinId)
    }
}
