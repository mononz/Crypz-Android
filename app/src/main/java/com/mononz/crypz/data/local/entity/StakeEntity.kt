package com.mononz.crypz.data.local.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.mononz.crypz.data.local.custom.StakeSummary
import com.mononz.crypz.data.local.entity.StakeEntity.Companion.TABLE_NAME
import com.mononz.crypz.data.remote.model.MsStake
import com.mononz.crypz.extension.newUtc
import org.json.JSONObject
import java.util.*

@Entity(tableName = TABLE_NAME)
class StakeEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "stake_id")
    var stakeId: Int? = null
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

        fun createEntity(market_coin_id: Int?, stake: Double?): StakeEntity {
            val entity = StakeEntity()
            entity.marketCoinId = market_coin_id
            entity.price = 0.0
            entity.stake = stake
            entity.createdAt = Date().newUtc()
            entity.updatedAt = Date().newUtc()
            return entity
        }

        fun createEntity(json: MsStake): StakeEntity {
            val entity = StakeEntity()
            entity.stakeId = json.stake_id
            entity.marketCoinId = json.market_coin_id
            entity.price = json.price
            entity.stake = json.stake
            entity.createdAt = json.created_at
            entity.updatedAt = Date().newUtc()
            return entity
        }

        fun createEntity(summary: StakeSummary): StakeEntity {
            val entity = StakeEntity()
            entity.stakeId = summary.stakeId
            entity.marketCoinId = summary.marketCoinId
            entity.price = summary.price
            entity.stake = summary.stake
            entity.createdAt = summary.createdAt
            entity.updatedAt = Date().newUtc()
            return entity
        }

        fun toJson(entity: StakeEntity): JSONObject {
            val json = JSONObject()
            try {
                json.put("stake_id", entity.stakeId)
                json.put("market_coin_id", entity.marketCoinId)
                json.put("price", entity.price)
                json.put("stake", entity.stake)
                json.put("created_at", entity.createdAt)
                json.put("updated_at", entity.updatedAt)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return json
        }
    }
}