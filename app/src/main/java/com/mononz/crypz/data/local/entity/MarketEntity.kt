package com.mononz.crypz.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mononz.crypz.data.local.entity.MarketEntity.Companion.TABLE_NAME

import com.mononz.crypz.data.remote.model.MsSync

@Entity(tableName = TABLE_NAME)
class MarketEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "market_id")
    var marketId: Int? = null
    @ColumnInfo(name = "name")
    var name: String? = null
    @ColumnInfo(name = "website")
    var website: String? = null
    @ColumnInfo(name = "image")
    var image: String? = null
    @ColumnInfo(name = "enabled")
    var enabled: Int? = null
    @ColumnInfo(name = "created_at")
    var createdAt: String? = null
    @ColumnInfo(name = "updated_at")
    var updatedAt: String? = null

    companion object {

        const val TABLE_NAME = "market"

        fun createEntity(json: MsSync.Market): MarketEntity {
            val entity = MarketEntity()
            entity.marketId = json.market_id
            entity.name = json.name
            entity.website = json.website
            entity.image = json.image
            entity.enabled = json.enabled
            entity.createdAt = json.created_at
            entity.updatedAt = json.updated_at
            return entity
        }
    }
}