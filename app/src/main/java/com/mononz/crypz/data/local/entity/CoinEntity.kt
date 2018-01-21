package com.mononz.crypz.data.local.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

import com.mononz.crypz.data.remote.model.MsSync

@Entity(tableName = "coin")
class CoinEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "coin_id")
    var coinId: Int? = null
    var code: String? = null
    var name: String? = null
    var icon: String? = null
    var enabled: Int? = null
    @ColumnInfo(name = "created_at")
    var createdAt: String? = null
    @ColumnInfo(name = "updated_at")
    var updatedAt: String? = null

    companion object {

        val TABLE_NAME = "coin"

        fun createEntity(json: MsSync.Coin): CoinEntity {
            val entity = CoinEntity()
            entity.coinId = json.coin_id
            entity.code = json.code
            entity.name = json.name
            entity.enabled = json.enabled
            entity.createdAt = json.created_at
            entity.updatedAt = json.updated_at
            return entity
        }
    }
}