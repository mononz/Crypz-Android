package com.mononz.crypz.data.local.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy

import com.mononz.crypz.data.local.entity.StakeEntity

@Dao
interface StakeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entities: List<StakeEntity>)

}