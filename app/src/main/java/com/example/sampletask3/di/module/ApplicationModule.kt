package com.example.sampletask3.di.module

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.sampletask3.BuildConfig
import com.example.sampletask3.MyApp
import com.example.sampletask3.data.local.prefs.UserPreferences
import com.example.sampletask3.data.remote.NetworkService
import com.example.sampletask3.data.remote.Networking
import com.example.sampletask3.data.repository.ProductRepository
import com.example.sampletask3.di.ApplicationContext
import com.example.sampletask3.utils.network.NetworkHelper
import com.example.sampletask3.utils.rx.RxSchedulerProvider
import com.example.sampletask3.utils.rx.SchedulerProvider
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Singleton

@Module
class ApplicationModule(private val application: MyApp) {

    @Provides
    @Singleton
    fun provideApplication(): Application = application

    @Provides
    @Singleton
    @ApplicationContext
    fun provideContext(): Context = application

    @Provides
    fun provideCompositeDisposable(): CompositeDisposable = CompositeDisposable()

    @Provides
    fun provideSchedulerProvider(): SchedulerProvider =
        RxSchedulerProvider()

    @Provides
    @Singleton
    fun provideSharedPreferences(): SharedPreferences =
        application.getSharedPreferences("sample-task-3-prefs", Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideNetworkService(): NetworkService =
        Networking.create(
            BuildConfig.BASE_URL,
            application.cacheDir,
            10 * 1024 * 1024 // 10MB
        )

    @Singleton
    @Provides
    fun provideNetworkHelper(): NetworkHelper =
        NetworkHelper(application)

    @Singleton
    @Provides
    fun providePostRepository(
        networkService: NetworkService,
        schedulerProvider: SchedulerProvider,
        userPreferences: UserPreferences,
        networkHelper: NetworkHelper
    ): ProductRepository =
        ProductRepository(networkService, schedulerProvider, userPreferences, networkHelper)
}