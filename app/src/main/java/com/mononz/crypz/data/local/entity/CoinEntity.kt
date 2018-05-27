package com.mononz.crypz.data.local.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.mononz.crypz.data.local.entity.CoinEntity.Companion.TABLE_NAME

import com.mononz.crypz.data.remote.model.MsSync

@Entity(tableName = TABLE_NAME)
class CoinEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "coin_id")
    var coinId: Int? = null
    @ColumnInfo(name = "code")
    var code: String? = null
    @ColumnInfo(name = "name")
    var name: String? = null
    @ColumnInfo(name = "icon")
    var icon: String? = null
    @ColumnInfo(name = "color")
    var color: String? = null
    @ColumnInfo(name = "enabled")
    var enabled: Int? = null
    @ColumnInfo(name = "created_at")
    var createdAt: String? = null
    @ColumnInfo(name = "updated_at")
    var updatedAt: String? = null

    companion object {

        const val TABLE_NAME = "coin"

        fun createEntity(json: MsSync.Coin): CoinEntity {
            val entity = CoinEntity()
            entity.coinId = json.coin_id
            entity.code = json.code
            entity.name = json.name
            entity.icon = json.icon
            entity.color = json.color
            entity.enabled = json.enabled
            entity.createdAt = json.created_at
            entity.updatedAt = json.updated_at
            return entity
        }
    }
}