package com.mononz.crypz.view.activity

import android.os.Bundle
import android.support.v7.widget.Toolbar
import butterknife.BindView
import butterknife.ButterKnife
import com.mononz.crypz.R
import com.mononz.crypz.base.BaseActivity
import com.mononz.crypz.viewmodel.GenericViewModel
import dagger.android.AndroidInjection

class MainActivity : BaseActivity<GenericViewModel>() {

    @BindView(R.id.toolbar) lateinit var toolbar: Toolbar

    override fun getViewModel(): Class<GenericViewModel> {
        return GenericViewModel::class.java
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)

        setSupportActionBar(toolbar)
    }
}