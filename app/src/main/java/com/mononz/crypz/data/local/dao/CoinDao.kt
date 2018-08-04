package com.mononz.crypz.data.local.dao

import androidx.room.*
import com.mononz.crypz.data.local.entity.CoinEntity
import io.reactivex.Single

@Dao
interface CoinDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entities: List<CoinEntity>)

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Transaction
    @Query("SELECT * FROM coin " +
            "INNER JOIN market_coin ON market_coin.coin_id = coin.coin_id " +
            "WHERE coin.enabled=1 AND market_coin.market_id=:marketId AND coin.name IS NOT NULL AND coin.code IS NOT NULL " +
            "ORDER BY coin.code ASC")
    fun getEnabledCoins(marketId : Int): Single<List<CoinEntity>>

}