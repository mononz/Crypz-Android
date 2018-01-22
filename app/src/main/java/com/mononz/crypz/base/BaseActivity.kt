package com.mononz.crypz.base

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.inputmethod.InputMethodManager
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasFragmentInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

abstract class BaseActivity<VM : ViewModel> : AppCompatActivity(), HasFragmentInjector,HasSupportFragmentInjector {

    @Inject lateinit var fragmentAndroidInjector: DispatchingAndroidInjector<android.app.Fragment>
    @Inject lateinit var fragmentSupportAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    var viewModel: VM? = null

    abstract fun getViewModel(): Class<VM>

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(getViewModel())
    }

    fun setSupportActionBar(toolbar: Toolbar, backArrowEnable: Boolean) {
        setSupportActionBar(toolbar)
        if (supportActionBar != null && backArrowEnable) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun fragmentInjector(): AndroidInjector<android.app.Fragment>? {
        return fragmentAndroidInjector
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment>? {
        return fragmentSupportAndroidInjector
    }

    fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

}