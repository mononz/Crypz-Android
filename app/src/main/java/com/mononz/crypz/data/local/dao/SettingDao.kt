package com.mononz.crypz.data.local.dao

import android.arch.persistence.room.*
import com.mononz.crypz.data.local.entity.SettingEntity

@Dao
interface SettingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entities: List<SettingEntity>)

}