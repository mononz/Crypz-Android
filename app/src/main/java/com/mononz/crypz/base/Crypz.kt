package com.mononz.crypz.base

import android.app.Activity
import android.app.Application
import com.mononz.crypz.injection.component.DaggerAppComponent
import com.mononz.crypz.library.StethoUtils
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class Crypz : Application(), HasActivityInjector {

    @Inject lateinit var activityDispatchingInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()

        StethoUtils.install(this)

        DaggerAppComponent.builder()
                .application(this)
                .build()
                .inject(this)
    }

    override fun activityInjector(): AndroidInjector<Activity>? {
        return activityDispatchingInjector
    }

    companion object {

        const val ADD_ACTIVITY_RC = 101

    }
}