package com.mononz.crypz.data.local.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import android.arch.persistence.room.Transaction
import com.mononz.crypz.data.local.custom.CoinSummary

import com.mononz.crypz.data.local.entity.MarketCoinEntity

@Dao
interface MarketCoinDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entities: List<MarketCoinEntity>)

    @Transaction
    @Query("SELECT * FROM market_coin")
    fun query(): LiveData<List<CoinSummary>>

}