package com.mononz.crypz.injection.module

import com.mononz.crypz.view.activity.AddEditActivity
import com.mononz.crypz.view.activity.MainActivity

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilderModule {

    @ContributesAndroidInjector(modules = [(FragmentBuilderModule::class)])
    abstract fun mainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [(FragmentBuilderModule::class)])
    abstract fun addEditActivity(): AddEditActivity

}