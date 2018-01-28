package com.mononz.crypz.data.local.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.mononz.crypz.data.local.custom.StakeSummary

import com.mononz.crypz.data.local.entity.StakeEntity
import io.reactivex.Single

@Dao
interface StakeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entities: List<StakeEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entities: StakeEntity)

    @Transaction
    @Query("SELECT stake.stake_id AS stakeId, coin.name AS coinName, coin.code AS coinCode, market.name AS marketName, stake.price AS price, stake.stake AS stake FROM stake " +
            "INNER JOIN market_coin ON market_coin.market_coin_id=stake.market_coin_id " +
            "INNER JOIN coin ON coin.coin_id=market_coin.coin_id " +
            "INNER JOIN market ON market.market_id=market_coin.market_id " +
            "WHERE coin.enabled=1 AND market.enabled=1 AND market_coin.enabled=1 " +
            "ORDER BY coin.code ASC, market.name ASC")
    fun getActiveTrackings(): LiveData<List<StakeSummary>>

    @Update()
    fun updateStakes(entities: List<StakeEntity>)

    @Transaction
    @Query("SELECT stake_id,market_coin_id,price,stake,created_at FROM stake")
    fun getStakesForNetwork(): Single<List<StakeEntity>>

}