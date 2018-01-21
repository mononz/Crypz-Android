package com.mononz.crypz.injection.module

import com.mononz.crypz.view.MainFragment

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuilderModule {

    @ContributesAndroidInjector
    abstract fun contributeSampleFragment(): MainFragment
}
