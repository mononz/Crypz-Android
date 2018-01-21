package com.mononz.crypz.data.local.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

import com.mononz.crypz.data.remote.model.MsSync

@Entity(tableName = "market_coin")
class MarketCoinEntity {

    @PrimaryKey(autoGenerate = true)
    var market_coin_id: Int? = null
    var market_id: Int? = null
    var coin_id: Int? = null
    var enabled: Int? = null
    var created_at: String? = null
    var updated_at: String? = null

    companion object {

        val TABLE_NAME = "market_coin"

        fun createEntity(json: MsSync.MarketCoin): MarketCoinEntity {
            val entity = MarketCoinEntity()
            entity.market_coin_id = json.market_coin_id
            entity.market_id = json.market_id
            entity.coin_id = json.coin_id
            entity.enabled = json.enabled
            entity.created_at = json.created_at
            entity.updated_at = json.updated_at
            return entity
        }
    }
}