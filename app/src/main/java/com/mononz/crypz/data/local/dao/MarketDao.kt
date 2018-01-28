package com.mononz.crypz.data.local.dao

import android.arch.persistence.room.*
import com.mononz.crypz.data.local.entity.MarketEntity
import io.reactivex.Single

@Dao
interface MarketDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entities: List<MarketEntity>)

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Transaction
    @Query("SELECT * FROM market " +
            "INNER JOIN market_coin ON market_coin.market_id = market.market_id " +
            "WHERE market.enabled=1 AND market.name IS NOT NULL " +
            "GROUP BY market.market_id " +
            "ORDER BY market.name ASC")
    fun getEnabledMarkets(): Single<List<MarketEntity>>

}