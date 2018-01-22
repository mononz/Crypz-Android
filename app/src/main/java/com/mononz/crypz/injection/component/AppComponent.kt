package com.mononz.crypz.injection.component

import android.app.Application
import com.mononz.crypz.base.Crypz
import com.mononz.crypz.injection.module.ActivityBuilderModule
import com.mononz.crypz.injection.module.AppModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
(AppModule::class),
(AndroidInjectionModule::class),
(ActivityBuilderModule::class)])
interface AppComponent {

    fun inject(crypz: Crypz)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}
