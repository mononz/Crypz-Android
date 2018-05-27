package com.mononz.crypz.view.activity

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Intent
import android.graphics.Color
import android.graphics.PointF
import android.os.Build
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import com.hookedonplay.decoviewlib.charts.EdgeDetail
import com.hookedonplay.decoviewlib.charts.SeriesItem
import com.hookedonplay.decoviewlib.charts.SeriesLabel
import com.mononz.crypz.R
import com.mononz.crypz.base.BaseActivity
import com.mononz.crypz.base.Crypz.Companion.ADD_ACTIVITY_RC
import com.mononz.crypz.data.Repository
import com.mononz.crypz.data.local.custom.StakeSummary
import com.mononz.crypz.data.local.entity.StakeEntity
import com.mononz.crypz.extension.pricify
import com.mononz.crypz.view.adapter.MainListAdapter
import com.mononz.crypz.viewmodel.MainViewModel
import dagger.android.AndroidInjection
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.include_toolbar.*
import kotlinx.android.synthetic.main.main_activity.*
import javax.inject.Inject

class MainActivity : BaseActivity<MainViewModel>(), MainListAdapter.Callback {

    @Inject lateinit var repository : Repository
    @Inject lateinit var adapter : MainListAdapter

    private var disposables = CompositeDisposable()
    private var stakeEntity : StakeEntity? = null

    val COLOR_BLUE = Color.parseColor("#1D76D2")
    val COLOR_PINK = Color.parseColor("#FF4081")
    val COLOR_YELLOW = Color.parseColor("#FFC107")
    val COLOR_EDGE = Color.parseColor("#22000000")
    val COLOR_BACK = Color.parseColor("#0166BB66")
    var mSeriesMax = 100f
    var mSeries1Index : Int = 0
    var mSeries2Index: Int = 0
    var mSeries3Index : Int = 0
    var mBack1Index : Int = 0
    
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

        adapter.setCallback(this)

        viewModel?.getActiveTrackings()?.observe(this, Observer<List<StakeSummary>> {
            it?.let {
                adapter.setData(it)

                // Create background track
                chartMe()
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

        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                deleteStake(viewHolder.adapterPosition)
            }
        })
        itemTouchHelper.attachToRecyclerView(recycler)

//        swiperefresh.setOnRefreshListener({
//            updateStakes()
//        })

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

        toolbar.post {
            //swiperefresh.isRefreshing = true
            updateStakes()
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
                //swiperefresh.isRefreshing = true
                updateStakes()
            }
        }
    }

    override fun totalsUpdated(total: Double) {
        total_card_value.text = total.pricify()
        total_card_layout.visibility = if (total == 0.0) View.GONE else View.VISIBLE
    }

    private fun deleteStake(position : Int) {
        val entity = adapter.removeAtPosition(position)
        stakeEntity = viewModel?.deleteStake(entity)
        val mSnackBar = Snackbar.make(coordinator, getString(R.string.main_snack_text), Snackbar.LENGTH_LONG)
        mSnackBar.setAction(getString(R.string.main_snack_action)) { snackAction() }
        mSnackBar.setActionTextColor(ContextCompat.getColor(this, R.color.white))
        val tv = mSnackBar.view.findViewById<TextView>(android.support.design.R.id.snackbar_text)
        tv.setTextColor(ContextCompat.getColor(this, R.color.white))
        mSnackBar.view.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
        mSnackBar.show()
    }

    private fun snackAction() {
        stakeEntity?.let {
            Observable.fromCallable(viewModel?.saveStake(it))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeBy (
                            onNext = {  },
                            onError =  { it.printStackTrace() },
                            onComplete = { stakeEntity = null })
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
                            //swiperefresh.isRefreshing = false
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
                            //swiperefresh.isRefreshing = false
                        })
        disposables.add(d)
    }

    fun chartMe() {

        chart.executeReset()
        chart.deleteAll()

        val circleInset = getDimension(23f) - (getDimension(46f) * 0.3f)
        val seriesBack1Item = SeriesItem.Builder(COLOR_BACK)
                .setRange(0f, mSeriesMax, mSeriesMax)
                .setChartStyle(SeriesItem.ChartStyle.STYLE_PIE)
                .setInset(PointF(circleInset, circleInset))
                .build()

        mBack1Index = chart.addSeries(seriesBack1Item)

        val series1Item = SeriesItem.Builder(COLOR_BLUE)
                .setRange(0f, mSeriesMax, 0f)
                .setInitialVisibility(false)
                .setLineWidth(getDimension(46f))
                .setSeriesLabel(SeriesLabel.Builder("Men").build())
                .setCapRounded(false)
                .addEdgeDetail(EdgeDetail(EdgeDetail.EdgeType.EDGE_INNER, COLOR_EDGE, 0.3f))
                .setShowPointWhenEmpty(false)
                .build()

        mSeries1Index = chart.addSeries(series1Item)

        val series2Item = SeriesItem.Builder(COLOR_PINK)
                .setRange(0f, mSeriesMax, 0f)
                .setInitialVisibility(false)
                .setLineWidth(getDimension(46f))
                .setSeriesLabel(SeriesLabel.Builder("Women").build())
                .setCapRounded(false)
                        //.setChartStyle(SeriesItem.ChartStyle.STYLE_PIE)
                .addEdgeDetail(EdgeDetail(EdgeDetail.EdgeType.EDGE_INNER, COLOR_EDGE, 0.3f))
                .setShowPointWhenEmpty(false)
                .build()

        mSeries2Index = chart.addSeries(series2Item)

        val series3Item = SeriesItem.Builder(COLOR_YELLOW)
                .setRange(0f, mSeriesMax, 0f)
                .setInitialVisibility(false)
                .setLineWidth(getDimension(46f))
                .setSeriesLabel(SeriesLabel.Builder("Children").build())
                .setCapRounded(false)
                        //.setChartStyle(SeriesItem.ChartStyle.STYLE_PIE)
                .addEdgeDetail(EdgeDetail(EdgeDetail.EdgeType.EDGE_INNER, COLOR_EDGE, 0.3f))
                .setShowPointWhenEmpty(false)
                .build()

        mSeries3Index = chart.addSeries(series3Item)
    }

    private fun getDimension(base:Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, base, resources.displayMetrics)
    }
}