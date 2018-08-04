package com.mononz.crypz.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import dagger.android.support.AndroidSupportInjection
import timber.log.Timber
import javax.inject.Inject

abstract class BaseFragment<VM : ViewModel> : androidx.fragment.app.Fragment() {

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
