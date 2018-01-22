package com.mononz.crypz.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mononz.crypz.R
import com.mononz.crypz.base.BaseAdapter
import com.mononz.crypz.data.local.custom.StakeSummary
import com.mononz.crypz.extension.pricify
import com.mononz.crypz.extension.pricify2
import kotlinx.android.synthetic.main.main_element.view.*
import java.util.*
import javax.inject.Inject

class MainListAdapter @Inject internal constructor() : BaseAdapter<MainListAdapter.ViewHolder, StakeSummary, MainListAdapter.Callback>() {

    private var mContent: List<StakeSummary> = ArrayList()
    private var mCallback: Callback? = null

    override fun setData(data: List<StakeSummary>) {
        this.mContent = data
        notifyDataSetChanged()
    }

    override fun setCallback(callback: Callback) {
        mCallback = callback
    }

    override fun getItemCount(): Int {
        return mContent.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(mContent[position], mCallback)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.main_element, viewGroup, false))
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(obj: StakeSummary, callback: Callback?) {
            itemView.coin.text = if (obj.coins!!.isNotEmpty()) obj.coins!![0].name else ""
            itemView.market.text = if (obj.markets!!.isNotEmpty()) obj.markets!![0].name else ""
            val current : Double? = if (obj.stakes!!.isNotEmpty()) obj.stakes!![0].price else 0.0
            val stake : Double? = if (obj.stakes!!.isNotEmpty()) obj.stakes!![0].stake else 0.0
            val total = current!! * stake!!

            itemView.stake.text = stake.toString() + if (obj.coins!!.isNotEmpty()) " " + obj.coins!![0].code?.toUpperCase() else ""
            itemView.current.text = current.pricify2()
            itemView.total.text = total.pricify()

            itemView.rootView.setOnClickListener(View.OnClickListener { callback?.clicked(obj.marketCoin?.marketCoinId) })
        }
    }

    interface Callback {
        fun clicked(marketCoinId: Int?)
    }

}