package com.mononz.crypz.view.activity

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.mononz.crypz.R
import com.mononz.crypz.base.BaseActivity
import com.mononz.crypz.data.local.custom.StakeSummary
import com.mononz.crypz.data.local.entity.StakeEntity
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
import android.app.Activity
import timber.log.Timber

class MainActivity : BaseActivity<MainViewModel>() {

    @Inject lateinit var adapter : MainListAdapter

    private var disposables = CompositeDisposable()

    private val REQUEST_CODE = 101

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
            it?.let {
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
                startActivityForResult(intent, REQUEST_CODE, options.toBundle())
                return@setOnClickListener
            }
            startActivityForResult(intent, REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Timber.d("onActivityResult, update")
            swiperefresh.isRefreshing = true
            updateStakes()
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