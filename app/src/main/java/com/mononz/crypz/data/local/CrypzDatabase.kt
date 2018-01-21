package com.mononz.crypz.data.local

import android.app.Application
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase

import com.mononz.crypz.data.local.dao.CoinDao
import com.mononz.crypz.data.local.dao.MarketCoinDao
import com.mononz.crypz.data.local.dao.MarketDao
import com.mononz.crypz.data.local.dao.StakeDao
import com.mononz.crypz.data.local.entity.CoinEntity
import com.mononz.crypz.data.local.entity.MarketCoinEntity
import com.mononz.crypz.data.local.entity.MarketEntity
import com.mononz.crypz.data.local.entity.StakeEntity

@Database(entities = arrayOf(
        CoinEntity::class,
        MarketEntity::class,
        MarketCoinEntity::class,
        StakeEntity::class),
        version = 1)
abstract class CrypzDatabase : RoomDatabase() {

    abstract fun coinDao(): CoinDao
    abstract fun marketDao(): MarketDao
    abstract fun marketCoinDao(): MarketCoinDao
    abstract fun stakeDao(): StakeDao

    companion object {

        private val databaseName = "crypz.db"

        fun buildDatabase(application: Application): CrypzDatabase {
            return Room.databaseBuilder(application, CrypzDatabase::class.java, databaseName).build()
        }
    }
}
