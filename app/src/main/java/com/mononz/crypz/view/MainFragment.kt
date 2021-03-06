package com.mononz.crypz.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.mononz.crypz.R
import com.mononz.crypz.base.BaseFragment
import com.mononz.crypz.controller.AnalyticsHelper
import com.mononz.crypz.viewmodel.GenericViewModel

import javax.inject.Inject

class MainFragment : BaseFragment<GenericViewModel>() {

    @Inject lateinit var analytics: AnalyticsHelper

    override fun getViewModel(): Class<GenericViewModel> {
        return GenericViewModel::class.java
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val rootView = inflater.inflate(R.layout.main_activity, container, false)

        return rootView
    }

    override fun onResume() {
        super.onResume()
        analytics.trackScreen("main fragment");
    }

    companion object {

        fun newInstance(): MainFragment {
            return MainFragment()
        }
    }

}