package com.mononz.crypz.data.local.dao

import android.arch.persistence.room.*
import com.mononz.crypz.data.local.entity.MarketCoinEntity
import io.reactivex.Single

@Dao
interface MarketCoinDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entities: List<MarketCoinEntity>)

    @Transaction
    @Query("SELECT * FROM market_coin WHERE market_id=:marketId AND coin_id=:coinId ORDER BY market_coin_id ASC LIMIT 1")
    fun validateMarketCoinSelection(marketId : Int, coinId : Int): Single<MarketCoinEntity>

}