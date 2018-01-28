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
            val current : Double? = if (obj.price != null) obj.price else 0.0
            val stake : Double? = if (obj.stake != null) obj.stake else 0.0
            val total = current!! * stake!!
            val displayStake = obj.marketName + " \u00b7 " + stake.toString()

            itemView.coin.text = (if (obj.coinCode != null) obj.coinCode?.toUpperCase() + " \u00b7 " else "") + obj.coinName
            itemView.market.text = displayStake
            itemView.current.text = current.pricify2()
            itemView.total.text = total.pricify()

            itemView.rootView.setOnClickListener({ callback?.clicked(obj.stakeId) })
        }
    }

    interface Callback {
        fun clicked(stakeId: Int?)
    }

}