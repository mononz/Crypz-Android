package com.mononz.crypz.data.local.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.ColumnInfo

@Entity(tableName = "stake")
class StakeEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "market_coin_id")
    var marketCoinId: Int? = null
    var price: Double? = null
    var stake: Double? = null
    @ColumnInfo(name = "created_at")
    var createdAt: String? = null
    @ColumnInfo(name = "updated_at")
    var updatedAt: String? = null

    companion object {

        val TABLE_NAME = "stake"

        fun createEntity(market_coin_id: Int?, price: Double?, stake: Double?): StakeEntity {
            val entity = StakeEntity()
            entity.marketCoinId = market_coin_id
            entity.price = price
            entity.stake = stake
            return entity
        }
    }
}