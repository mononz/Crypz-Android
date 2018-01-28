package com.mononz.crypz.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.mononz.crypz.data.Repository
import com.mononz.crypz.data.local.custom.StakeSummary

import com.mononz.crypz.data.local.entity.StakeEntity
import com.mononz.crypz.data.remote.model.MsStake
import io.reactivex.Observable
import io.reactivex.Single

import javax.inject.Inject

class MainViewModel @Inject constructor() : ViewModel() {

    @Inject lateinit var repository: Repository

    fun getActiveTrackings(): LiveData<List<StakeSummary>> {
        return repository.getActiveTrackings()
    }

    fun getStakesForNetwork(): Single<List<StakeEntity>> {
        return repository.getStakesForNetwork()
    }

    fun getStakes(stakes : List<StakeEntity>) : Observable<List<MsStake>> {
        return repository.renewStakePrices(stakes)
    }

    fun updateStakes(prices : List<MsStake>) {
        val entities = ArrayList<StakeEntity>()
        prices.forEach({
            entities.add(StakeEntity.createEntity(it))
        })
        return repository.updateStakes(entities)
    }
}