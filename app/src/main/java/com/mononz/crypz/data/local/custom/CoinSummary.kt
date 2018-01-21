package com.mononz.crypz.data.local.custom

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Relation

import com.mononz.crypz.data.local.entity.CoinEntity
import com.mononz.crypz.data.local.entity.MarketCoinEntity
import com.mononz.crypz.data.local.entity.MarketEntity

class CoinSummary {

    @Embedded
    var marketCoin: MarketCoinEntity? = null

    @Relation(parentColumn = "market_id", entityColumn = "market_id", entity = MarketEntity::class)
    var markets: List<MarketEntity>? = null

    @Relation(parentColumn = "coin_id", entityColumn = "coin_id", entity = CoinEntity::class)
    var coins: List<CoinEntity>? = null

}