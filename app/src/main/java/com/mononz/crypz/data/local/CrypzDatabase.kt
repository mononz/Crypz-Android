package com.mononz.crypz.data.local

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mononz.crypz.data.local.dao.*

import com.mononz.crypz.data.local.entity.*

@Database(entities = [
    (CoinEntity::class),
    (MarketEntity::class),
    (MarketCoinEntity::class),
    (SettingEntity::class),
    (StakeEntity::class)],
        version = 1)
abstract class CrypzDatabase : RoomDatabase() {

    abstract fun coinDao(): CoinDao
    abstract fun marketDao(): MarketDao
    abstract fun marketCoinDao(): MarketCoinDao
    abstract fun settingDao(): SettingDao
    abstract fun stakeDao(): StakeDao

    companion object {

        private const val databaseName = "crypz.db"

        fun buildDatabase(application: Application): CrypzDatabase {
            return Room.databaseBuilder(application, CrypzDatabase::class.java, databaseName).build()
        }
    }
}
