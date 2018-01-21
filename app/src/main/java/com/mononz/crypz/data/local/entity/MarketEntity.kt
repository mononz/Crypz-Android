package com.mononz.crypz.data.local.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

import com.mononz.crypz.data.remote.model.MsSync

@Entity(tableName = "market")
class MarketEntity {

    @PrimaryKey(autoGenerate = true)
    var market_id: Int? = null
    var name: String? = null
    var website: String? = null
    var image: String? = null
    var enabled: Int? = null
    var created_at: String? = null
    var updated_at: String? = null

    companion object {

        val TABLE_NAME = "market"

        fun createEntity(json: MsSync.Market): MarketEntity {
            val entity = MarketEntity()
            entity.market_id = json.market_id
            entity.name = json.name
            entity.website = json.website
            entity.image = json.image
            entity.enabled = json.enabled
            entity.created_at = json.created_at
            entity.updated_at = json.updated_at
            return entity
        }
    }
}