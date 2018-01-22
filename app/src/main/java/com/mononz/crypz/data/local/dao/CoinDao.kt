package com.mononz.crypz.data.local.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.mononz.crypz.data.local.entity.CoinEntity

@Dao
interface CoinDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entities: List<CoinEntity>)

    @Transaction
    @Query("SELECT * FROM coin ORDER BY name")
    fun query(): LiveData<CoinEntity>

}