package com.mononz.crypz.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mononz.crypz.data.local.entity.MarketCoinEntity.Companion.TABLE_NAME

import com.mononz.crypz.data.remote.model.MsSync

@Entity(tableName = TABLE_NAME)
class MarketCoinEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "market_coin_id")
    var marketCoinId: Int? = null
    @ColumnInfo(name = "market_id")
    var marketId: Int? = null
    @ColumnInfo(name = "coin_id")
    var coinId: Int? = null
    @ColumnInfo(name = "enabled")
    var enabled: Int? = null
    @ColumnInfo(name = "created_at")
    var createdAt: String? = null
    @ColumnInfo(name = "updated_at")
    var updatedAt: String? = null

    companion object {

        const val TABLE_NAME = "market_coin"

        fun createEntity(json: MsSync.MarketCoin): MarketCoinEntity {
            val entity = MarketCoinEntity()
            entity.marketCoinId = json.market_coin_id
            entity.marketId = json.market_id
            entity.coinId = json.coin_id
            entity.enabled = json.enabled
            entity.createdAt = json.created_at
            entity.updatedAt = json.updated_at
            return entity
        }
    }
}