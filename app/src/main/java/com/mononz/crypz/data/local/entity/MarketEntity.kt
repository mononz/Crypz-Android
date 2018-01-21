package com.mononz.crypz.data.local.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

import com.mononz.crypz.data.remote.model.MsSync

@Entity(tableName = "market")
class MarketEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "market_id")
    var marketId: Int? = null
    var name: String? = null
    var website: String? = null
    var image: String? = null
    var enabled: Int? = null
    @ColumnInfo(name = "created_at")
    var createdAt: String? = null
    @ColumnInfo(name = "updated_at")
    var updatedAt: String? = null

    companion object {

        val TABLE_NAME = "market"

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