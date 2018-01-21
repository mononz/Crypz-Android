package com.mononz.crypz.data.local.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import android.arch.persistence.room.Transaction
import com.mononz.crypz.data.local.custom.StakeSummary

import com.mononz.crypz.data.local.entity.MarketCoinEntity

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

}