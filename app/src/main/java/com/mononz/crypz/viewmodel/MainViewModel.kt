package com.mononz.crypz.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.mononz.crypz.data.Repository

import com.mononz.crypz.data.local.custom.StakeSummary
import com.mononz.crypz.data.local.entity.StakeEntity
import com.mononz.crypz.data.remote.model.MsPrices
import io.reactivex.Observable
import io.reactivex.Single

import javax.inject.Inject

class MainViewModel @Inject constructor() : ViewModel() {

    @Inject lateinit var repository: Repository

    fun getCoins(): LiveData<List<StakeSummary>> {
        return repository.getCoins()
    }

    fun getStakes(): Single<List<StakeEntity>> {
        return repository.getStakesForNetwork()
    }

    fun getPrices(stakes : List<StakeEntity>) : Observable<List<MsPrices>> {
        return repository.getPrices(stakes)
    }

    fun updateStakes(prices : List<MsPrices>) {
        val entities = ArrayList<StakeEntity>()
        prices.forEach({
            entities.add(StakeEntity.createEntity(it))
        })
        return repository.updateStakes(entities)
    }
}