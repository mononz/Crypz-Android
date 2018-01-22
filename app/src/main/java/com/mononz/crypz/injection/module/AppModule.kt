package com.mononz.crypz.injection.module

import android.app.Application
import com.mononz.crypz.BuildConfig
import com.mononz.crypz.controller.AnalyticsHelper
import com.mononz.crypz.controller.DeviceHelper
import com.mononz.crypz.controller.ErrorHelper
import com.mononz.crypz.controller.PreferenceHelper
import com.mononz.crypz.data.local.CrypzDatabase
import com.mononz.crypz.data.remote.NetworkInterface
import com.mononz.crypz.data.remote.RequestInterceptor
import com.mononz.crypz.library.StethoUtils
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module(includes = [(ViewModelModule::class)])
class AppModule {

    @Provides
    @Singleton
    fun provideDatabase(application: Application): CrypzDatabase {
        return CrypzDatabase.buildDatabase(application)
    }

    @Provides
    @Singleton
    fun providesAnalytics(application: Application): AnalyticsHelper {
        return AnalyticsHelper(application)
    }

    @Provides
    @Singleton
    fun providesPreferences(application: Application): PreferenceHelper {
        return PreferenceHelper(application)
    }

    @Provides
    @Singleton
    fun providesOkHttpClient(session: PreferenceHelper): OkHttpClient {
        var builder: OkHttpClient.Builder = OkHttpClient().newBuilder()
                .readTimeout(30L, TimeUnit.SECONDS)
                .connectTimeout(30L, TimeUnit.SECONDS)
                .writeTimeout(30L, TimeUnit.SECONDS)
                .addInterceptor(RequestInterceptor(session))

        if (BuildConfig.DEBUG) {
            builder = StethoUtils.installInterceptors(builder)
        }
        return builder.build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .baseUrl(BuildConfig.SERVER_PATH)
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    @Provides
    @Singleton
    fun providesNetwork(retrofit: Retrofit): NetworkInterface {
        return retrofit.create(NetworkInterface::class.java)
    }

    @Provides
    @Singleton
    fun providesError(analytics: AnalyticsHelper, retrofit: Retrofit): ErrorHelper {
        return ErrorHelper(analytics, retrofit)
    }

    @Provides
    @Singleton
    fun providesDevice(application: Application): DeviceHelper {
        return DeviceHelper(application)
    }

}