package com.mononz.crypz.view.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mononz.crypz.R
import com.mononz.crypz.base.BaseAdapter
import com.mononz.crypz.data.local.custom.StakeSummary
import com.mononz.crypz.extension.loadUrl
import com.mononz.crypz.extension.pricify
import com.mononz.crypz.extension.thousands
import kotlinx.android.synthetic.main.main_element.view.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class MainListAdapter @Inject internal constructor() : BaseAdapter<MainListAdapter.ViewHolder, StakeSummary, MainListAdapter.Callback>() {

    private var mContent: ArrayList<StakeSummary> = ArrayList()
    private var mCallback: Callback? = null

    override fun setData(data: List<StakeSummary>) {
        this.mContent = ArrayList(data)
        notifyDataSetChanged()
        updateTotals()
    }

    private fun updateTotals() {
        var total = 0.0
        mContent.forEach {
            val price = if (it.price != null) it.price!! else 0.0
            val stake = if (it.stake != null) it.stake!! else 0.0
            total += price * stake
        }
        mCallback?.totalsUpdated(total)
    }

    override fun setCallback(callback: Callback) {
        mCallback = callback
    }

    override fun getItemCount(): Int {
        return mContent.size
    }

    fun removeAtPosition(position : Int) : StakeSummary {
        val entity = mContent[position]
        notifyItemRemoved(position)
        mContent.removeAt(position)
        updateTotals()
        return entity
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(mContent[position])
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.main_element, viewGroup, false))
    }

    class ViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {

        fun bind(obj: StakeSummary) {
            val current : Double? = if (obj.price != null) obj.price else 0.0
            val stake : Double? = if (obj.stake != null) obj.stake else 0.0
            val total = current!! * stake!!

            val displayStake = obj.marketName + " \u00b7 " + stake.thousands()
            val displayTitle = (if (obj.coinCode != null) obj.coinCode?.toUpperCase() + " \u00b7 " else "") + obj.coinName

            itemView.coin.text = displayTitle
            itemView.market.text = displayStake

            itemView.current.text = current.pricify()
            itemView.total.text = total.pricify()

            itemView.icon.loadUrl(obj.coinIcon)
            itemView.icon.contentDescription = obj.coinName
        }
    }

    interface Callback {
        fun totalsUpdated(total: Double)
    }

}