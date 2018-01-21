package com.mononz.crypz.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.mononz.crypz.R
import com.mononz.crypz.base.BaseAdapter
import com.mononz.crypz.data.local.custom.CoinSummary
import com.mononz.crypz.extension.pricify

import java.util.ArrayList

import javax.inject.Inject

import kotlinx.android.synthetic.main.main_element.view.*

class MainListAdapter @Inject internal constructor() : BaseAdapter<MainListAdapter.ViewHolder, CoinSummary, MainListAdapter.Callback>() {

    private var mContent: List<CoinSummary> = ArrayList()

    override fun setData(data: List<CoinSummary>) {
        this.mContent = data
        notifyDataSetChanged()
    }

    override fun setCallback(callback: Callback) {}

    override fun getItemCount(): Int {
        return mContent.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(mContent[position])
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.main_element, viewGroup, false))
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(obj: CoinSummary) {
            itemView.code.text = if (obj.coins!!.isNotEmpty()) obj.coins!![0].code?.toUpperCase() else ""
            itemView.coin.text = if (obj.coins!!.isNotEmpty()) obj.coins!![0].name else ""
            itemView.market.text = if (obj.markets!!.isNotEmpty()) obj.markets!![0].name else ""
            val current = 275.53
            val stake = 13.2
            val total = current*stake
            itemView.current.text = current.pricify()
            itemView.stake.text = stake.toString()
            itemView.total.text = total.pricify()
        }
    }

    interface Callback

}