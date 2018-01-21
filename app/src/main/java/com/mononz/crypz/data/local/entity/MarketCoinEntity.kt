package com.mononz.crypz.data.local.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

import com.mononz.crypz.data.remote.model.MsSync

@Entity(tableName = "market_coin")
class MarketCoinEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "market_coin_id")
    var marketCoinId: Int? = null
    @ColumnInfo(name = "market_id")
    var marketId: Int? = null
    var coin_id: Int? = null
    var enabled: Int? = null
    @ColumnInfo(name = "created_at")
    var createdAt: String? = null
    @ColumnInfo(name = "updated_at")
    var updatedAt: String? = null

    companion object {

        val TABLE_NAME = "market_coin"

        fun createEntity(json: MsSync.MarketCoin): MarketCoinEntity {
            val entity = MarketCoinEntity()
            entity.marketCoinId = json.market_coin_id
            entity.marketId = json.market_id
            entity.coin_id = json.coin_id
            entity.enabled = json.enabled
            entity.createdAt = json.created_at
            entity.updatedAt = json.updated_at
            return entity
        }
    }
}