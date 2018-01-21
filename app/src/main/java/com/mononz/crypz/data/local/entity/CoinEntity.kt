package com.mononz.crypz.data.local.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

import com.mononz.crypz.data.remote.model.MsSync

@Entity(tableName = "coin")
class CoinEntity {

    @PrimaryKey(autoGenerate = true)
    var coin_id: Int? = null
    var code: String? = null
    var name: String? = null
    var icon: String? = null
    var enabled: Int? = null
    var created_at: String? = null
    var updated_at: String? = null

    companion object {

        val TABLE_NAME = "coin"

        fun createEntity(json: MsSync.Coin): CoinEntity {
            val entity = CoinEntity()
            entity.coin_id = json.coin_id
            entity.code = json.code
            entity.name = json.name
            entity.enabled = json.enabled
            entity.created_at = json.created_at
            entity.updated_at = json.updated_at
            return entity
        }
    }
}