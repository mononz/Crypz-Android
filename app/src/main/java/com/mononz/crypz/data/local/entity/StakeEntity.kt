package com.mononz.crypz.data.local.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.mononz.crypz.data.local.entity.StakeEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
class StakeEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "market_coin_id")
    var marketCoinId: Int? = null
    @ColumnInfo(name = "price")
    var price: Double? = null
    @ColumnInfo(name = "stake")
    var stake: Double? = null
    @ColumnInfo(name = "created_at")
    var createdAt: String? = null
    @ColumnInfo(name = "updated_at")
    var updatedAt: String? = null

    companion object {

        const val TABLE_NAME = "stake"

        fun createEntity(market_coin_id: Int?, price: Double?, stake: Double?): StakeEntity {
            val entity = StakeEntity()
            entity.marketCoinId = market_coin_id
            entity.price = price
            entity.stake = stake
            return entity
        }
    }
}