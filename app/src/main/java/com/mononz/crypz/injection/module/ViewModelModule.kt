package com.mononz.crypz.injection.module

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.mononz.crypz.injection.ViewModelFactory

import com.mononz.crypz.injection.ViewModelKey
import com.mononz.crypz.viewmodel.GenericViewModel

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(GenericViewModel::class)
    internal abstract fun bindsGenericViewModel(viewModel: GenericViewModel): ViewModel

    @Binds
    internal abstract fun bindsViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

}