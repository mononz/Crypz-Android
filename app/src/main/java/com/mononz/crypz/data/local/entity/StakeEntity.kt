package com.mononz.crypz.data.local.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "stake")
class StakeEntity {

    @PrimaryKey(autoGenerate = true)
    var market_coin_id: Int? = null
    var price: Double? = null
    var stake: Double? = null

    companion object {

        val TABLE_NAME = "stake"

        fun createEntity(market_coin_id: Int?, price: Double?, stake: Double?): StakeEntity {
            val entity = StakeEntity()
            entity.market_coin_id = market_coin_id
            entity.price = price
            entity.stake = stake
            return entity
        }
    }
}