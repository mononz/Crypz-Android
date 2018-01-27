package com.mononz.crypz.injection.module

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.mononz.crypz.injection.ViewModelFactory

import com.mononz.crypz.injection.ViewModelKey
import com.mononz.crypz.viewmodel.AddViewModel
import com.mononz.crypz.viewmodel.GenericViewModel
import com.mononz.crypz.viewmodel.MainViewModel

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
    @IntoMap
    @ViewModelKey(AddViewModel::class)
    internal abstract fun bindsMAddViewModel(viewModel: AddViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    internal abstract fun bindsMainViewModel(viewModel: MainViewModel): ViewModel

    @Binds
    internal abstract fun bindsViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

}