package com.mononz.crypz.view.activity

import android.os.Build
import android.os.Bundle
import android.transition.Slide
import android.view.Gravity
import android.widget.ArrayAdapter
import com.mononz.crypz.R
import com.mononz.crypz.base.BaseActivity
import com.mononz.crypz.viewmodel.GenericViewModel
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.add_edit.*
import kotlinx.android.synthetic.main.include_toolbar.*


class AddEditActivity : BaseActivity<GenericViewModel>() {

    override fun getViewModel(): Class<GenericViewModel> {
        return GenericViewModel::class.java
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_edit)

        setSupportActionBar(toolbar)

        // enter and exit transitions for lollipop and above devices
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.enterTransition = Slide(Gravity.END)
            window.returnTransition = Slide(Gravity.END)
        }

        fab.setOnClickListener {
            //Toast.makeText(this, "Add", Toast.LENGTH_SHORT).show()
            onBackPressed()
        }

        val markets = arrayOf("CoinBase", "BTC Markets")
        val adapterMarkets = ArrayAdapter(this, android.R.layout.simple_spinner_item, markets)
        adapterMarkets.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        market_dropdown.adapter = adapterMarkets

        val coins = arrayOf("ETH: Ethereum", "BTC: Bitcoin", "XRP: Ripple")
        val adapterCoins = ArrayAdapter(this, android.R.layout.simple_spinner_item, coins)
        adapterCoins.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        coin_dropdown.adapter = adapterCoins
    }
}