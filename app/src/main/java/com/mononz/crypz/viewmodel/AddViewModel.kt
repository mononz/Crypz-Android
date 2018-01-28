package com.mononz.crypz.viewmodel

import android.arch.lifecycle.ViewModel
import com.mononz.crypz.data.Repository
import com.mononz.crypz.data.local.entity.CoinEntity
import com.mononz.crypz.data.local.entity.MarketCoinEntity
import com.mononz.crypz.data.local.entity.MarketEntity
import com.mononz.crypz.data.local.entity.StakeEntity
import io.reactivex.Single
import java.util.concurrent.Callable
import javax.inject.Inject

class AddViewModel @Inject constructor() : ViewModel() {

    @Inject lateinit var repository: Repository

    fun getEnabledMarkets() : Single<List<MarketEntity>> {
        return repository.getEnabledMarkets()
    }

    fun getEnabledCoins(marketId : Int) : Single<List<CoinEntity>> {
        return repository.getEnabledCoins(marketId)
    }

    fun validateMarketCoinSelection(marketId : Int, coinId : Int): Single<MarketCoinEntity> {
        return repository.validateMarketCoinSelection(marketId, coinId)
    }

    fun fakeStake(stake : StakeEntity) : Callable<Unit> {
        return repository.insertStake(stake)
    }
}