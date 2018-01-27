package com.mononz.crypz.data.local.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.mononz.crypz.data.local.custom.StakeSummary
import com.mononz.crypz.data.local.entity.MarketCoinEntity
import io.reactivex.Single

@Dao
interface MarketCoinDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entities: List<MarketCoinEntity>)

    @Transaction
    @Query("SELECT * FROM market_coin " +
            "INNER JOIN stake ON stake.market_coin_id=market_coin.market_coin_id " +
            "INNER JOIN coin ON coin.coin_id=market_coin.coin_id " +
            "INNER JOIN market ON market.market_id=market_coin.market_id " +
            "WHERE coin.enabled=1 AND market.enabled=1 AND market_coin.enabled=1")
    fun query(): LiveData<List<StakeSummary>>

    @Transaction
    @Query("SELECT * FROM stake " +
            "INNER JOIN market_coin ON market_coin.market_coin_id=stake.market_coin_id " +
            "INNER JOIN coin ON coin.coin_id=market_coin.coin_id " +
            "INNER JOIN market ON market.market_id=market_coin.market_id " +
            "WHERE coin.enabled=1 AND market.enabled=1 AND market_coin.enabled=1")
    fun getActiveTrackings(): LiveData<List<StakeSummary>>

    @Transaction
    @Query("SELECT * FROM market_coin WHERE market_id=:marketId AND coin_id=:coinId ORDER BY market_coin_id ASC LIMIT 1")
    fun validateMarketCoinSelection(marketId : Int, coinId : Int): Single<MarketCoinEntity>

}