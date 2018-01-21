package com.mononz.crypz.data.local.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import android.arch.persistence.room.Transaction

import com.mononz.crypz.data.local.entity.MarketEntity

@Dao
interface MarketDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entities: List<MarketEntity>)

    @Transaction
    @Query("SELECT * FROM market")
    fun query(): LiveData<MarketEntity>

}