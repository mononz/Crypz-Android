package com.mononz.crypz.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.transition.Slide
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import com.mononz.crypz.R
import com.mononz.crypz.base.BaseActivity
import com.mononz.crypz.data.Repository
import com.mononz.crypz.data.local.entity.StakeEntity
import com.mononz.crypz.viewmodel.AddViewModel
import dagger.android.AndroidInjection
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.add_activity.*
import kotlinx.android.synthetic.main.include_toolbar.*
import timber.log.Timber
import javax.inject.Inject

class AddActivity : BaseActivity<AddViewModel>() {

    @Inject lateinit var repository: Repository

    private var marketIds : ArrayList<Int?> = ArrayList()
    private var marketNames : Array<String?> = emptyArray()
    private var marketAdapter : ArrayAdapter<String?> ?= null

    private var coinIds : ArrayList<Int?> = ArrayList()
    private var coinNames : Array<String?> = emptyArray()
    private var coinAdapter : ArrayAdapter<String?> ?= null

    override fun getViewModel(): Class<AddViewModel> {
        return AddViewModel::class.java
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_activity)

        setSupportActionBar(toolbar)

        // enter and exit transitions for lollipop and above devices
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.enterTransition = Slide(Gravity.END)
            window.returnTransition = Slide(Gravity.END)
        }

        fab.setOnClickListener {
            if (market_dropdown.selectedItemPosition < marketIds.size && coin_dropdown.selectedItemPosition < coinIds.size) {
                validateSelections(marketIds[market_dropdown.selectedItemPosition]!!, coinIds[coin_dropdown.selectedItemPosition]!!, stake_input.text.toString())
            }
        }

        stake_input.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                stake_input_layout.error = null
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) { }
            override fun afterTextChanged(s: Editable) { }
        })

        setAdapterCoin(ArrayList(), ArrayList())
        setAdapterMarket(ArrayList(), ArrayList())

        showMarkets()
    }

    private fun setAdapterMarket(ids : ArrayList<Int?>, names : ArrayList<String?>) {
        marketIds = ids
        marketNames = arrayOfNulls(names.size)
        marketNames = names.toArray(marketNames)

        marketAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, marketNames)
        marketAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        market_dropdown.adapter = marketAdapter
        market_dropdown.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                if (position < marketIds.size) {
                    marketIds[position]?.let { showCoins(it) }
                }
            }
            override fun onNothingSelected(parentView: AdapterView<*>) { }
        }
    }

    private fun setAdapterCoin(ids : ArrayList<Int?>, names : ArrayList<String?>) {
        coinIds = ids
        coinNames = arrayOfNulls(names.size)
        coinNames = names.toArray(coinNames)

        coinAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, coinNames)
        coinAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        coin_dropdown.adapter = coinAdapter
    }

    private fun showMarkets() {
        viewModel?.getEnabledMarkets()
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeBy(
                        onSuccess = {
                            val ids = ArrayList<Int?>()
                            val names = ArrayList<String?>()
                            it.forEach {
                                ids.add(it.marketId)
                                names.add(it.name)
                            }
                            setAdapterMarket(ids, names)
                            if (it.isNotEmpty()) {
                                marketIds[0]?.let { showCoins(it) }
                            }
                        },
                        onError = {
                            it.printStackTrace()
                        })
    }

    private fun showCoins(marketId : Int) {
        viewModel?.getEnabledCoins(marketId)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeBy(
                        onSuccess = {
                            val ids = ArrayList<Int?>()
                            val names = ArrayList<String?>()
                            it.forEach {
                                ids.add(it.coinId)
                                names.add("${it.code?.toUpperCase()} \u00b7 ${it.name}")
                            }
                            setAdapterCoin(ids, names)
                        },
                        onError = {
                            it.printStackTrace()
                        })
    }

    private fun validateSelections(marketId : Int, coinId : Int, stake : String) {
        val owned : Double
        try {
            Timber.d("stake %s", stake)
            owned = stake.toDouble()
        } catch (e : NumberFormatException ) {
            e.printStackTrace()
            stake_input_layout.error = "Invalid Number"
            return
        } catch (e : Exception ) {
            e.printStackTrace()
            stake_input_layout.error = "Invalid Input"
            return
        }

        viewModel?.validateMarketCoinSelection(marketId, coinId)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeBy(
                        onSuccess = {
                            saveStake(StakeEntity.createEntity(it.marketCoinId, owned))
                        },
                        onError = {
                            it.printStackTrace()
                        })
    }

    private fun saveStake(entity : StakeEntity) {
        Observable.fromCallable(viewModel?.saveStake(entity))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy (
                        onNext = { println(it) },
                        onError =  { it.printStackTrace() },
                        onComplete = { allDone() })
    }

    private fun allDone() {
        val returnIntent = Intent()
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }
}