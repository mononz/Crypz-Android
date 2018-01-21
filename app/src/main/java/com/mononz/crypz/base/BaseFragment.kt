package com.mononz.crypz.base

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment

import javax.inject.Inject

import dagger.android.support.AndroidSupportInjection
import timber.log.Timber

abstract class BaseFragment<VM : ViewModel> : Fragment() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    var viewModel: VM? = null

    abstract fun getViewModel(): Class<VM>

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(getViewModel())
    }

    fun hideKeyboard() {
        if (activity is BaseActivity<*>) {
            (activity as BaseActivity<*>).hideKeyboard()
        } else {
            Timber.e("Parent Activity does not inherit from BaseActivity")
        }
    }
}
