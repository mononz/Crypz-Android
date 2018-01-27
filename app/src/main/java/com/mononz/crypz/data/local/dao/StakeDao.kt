package com.mononz.crypz.data.local.dao

import android.arch.persistence.room.*

import com.mononz.crypz.data.local.entity.StakeEntity
import io.reactivex.Single

@Dao
interface StakeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entities: List<StakeEntity>)

    @Transaction
    @Query("SELECT stake_id,market_coin_id,price FROM stake")
    fun getStakesForNetwork(): Single<List<StakeEntity>>

}