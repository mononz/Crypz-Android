package com.mononz.crypz.data.local.dao

import android.arch.persistence.room.*

import com.mononz.crypz.data.local.entity.StakeEntity
import io.reactivex.Single

@Dao
interface StakeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entities: List<StakeEntity>)

    @Transaction
    @Query("SELECT market_coin_id FROM stake")
    fun getStakes(): Single<List<StakeEntity>>

}