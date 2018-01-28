package com.mononz.crypz.view.activity

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.mononz.crypz.R
import com.mononz.crypz.base.BaseActivity
import com.mononz.crypz.base.Crypz.Companion.ADD_ACTIVITY_RC
import com.mononz.crypz.data.Repository
import com.mononz.crypz.data.local.custom.StakeSummary
import com.mononz.crypz.data.local.entity.StakeEntity
import com.mononz.crypz.extension.pricify2
import com.mononz.crypz.view.adapter.MainListAdapter
import com.mononz.crypz.viewmodel.MainViewModel
import dagger.android.AndroidInjection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.include_toolbar.*
import kotlinx.android.synthetic.main.main_activity.*
import javax.inject.Inject

class MainActivity : BaseActivity<MainViewModel>() {

    @Inject lateinit var repository: Repository

    @Inject lateinit var adapter : MainListAdapter

    private var disposables = CompositeDisposable()

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

        viewModel?.getActiveTrackings()?.observe(this, Observer<List<StakeSummary>> {
            it?.let {
                if (it.isNotEmpty()) {
                    total_card_layout.visibility = View.VISIBLE
                    var total = 17561.355
                    // ToDo add up totals here
                    total_card_value.text = total.pricify2()
                } else {
                    total_card_layout.visibility = View.GONE
                }
                adapter.setData(it)
            }
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

        swiperefresh.setOnRefreshListener({
            updateStakes()
        })

        fab.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this)
                @Suppress("ImplicitThis")
                startActivityForResult(intent, ADD_ACTIVITY_RC, options.toBundle())
                return@setOnClickListener
            }
            startActivityForResult(intent, ADD_ACTIVITY_RC)
        }
    }

    override fun onResume() {
        super.onResume()
        repository.sync(false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_ACTIVITY_RC) {
            if (resultCode == Activity.RESULT_OK) {
                swiperefresh.isRefreshing = true
                updateStakes()
            }
        }
    }

    private fun updateStakes() {
        viewModel?.getStakesForNetwork()
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeBy(
                        onSuccess = {
                            updateStakePrices(it)
                        },
                        onError = {
                            it.printStackTrace()
                            swiperefresh.isRefreshing = false
                        })
    }

    private fun updateStakePrices(stakes: List<StakeEntity>) {
        val d : Disposable = viewModel?.getStakes(stakes)!!
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy (
                        onNext = {
                            viewModel?.updateStakes(it)
                        },
                        onError = {
                            it.printStackTrace()
                        },
                        onComplete = {
                            swiperefresh.isRefreshing = false
                        })
        disposables.add(d)
    }
}