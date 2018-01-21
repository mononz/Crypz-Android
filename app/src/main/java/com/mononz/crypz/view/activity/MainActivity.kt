package com.mononz.crypz.view.activity

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.*
import com.mononz.crypz.R
import com.mononz.crypz.base.BaseActivity
import com.mononz.crypz.data.local.custom.StakeSummary
import com.mononz.crypz.view.adapter.MainListAdapter
import com.mononz.crypz.viewmodel.MainViewModel
import dagger.android.AndroidInjection
import javax.inject.Inject
import android.support.v7.widget.DividerItemDecoration
import kotlinx.android.synthetic.main.include_toolbar.*
import kotlinx.android.synthetic.main.main_activity.*
import android.support.v7.widget.RecyclerView
import android.widget.Toast

class MainActivity : BaseActivity<MainViewModel>() {

    @Inject lateinit var adapter : MainListAdapter

    override fun getViewModel(): Class<MainViewModel> {
        return MainViewModel::class.java
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        setSupportActionBar(toolbar)

        val llm = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recycler.setHasFixedSize(true)
        recycler.layoutManager = llm
        recycler.addItemDecoration(DividerItemDecoration(recycler.context, llm.orientation))
        recycler.adapter = adapter
        recycler.isNestedScrollingEnabled = false

        viewModel?.getCoins()?.observe(this, Observer<List<StakeSummary>> {
            it?.let { adapter.setData(it) }
        })

        recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                if (dy > 0) {
                    fab.hide()
                } else {
                    fab.show()
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })

        fab.setOnClickListener {
            Toast.makeText(this, "Add new", Toast.LENGTH_SHORT).show()
        }

    }
}