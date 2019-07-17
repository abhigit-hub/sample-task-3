package com.example.sampletask3.di.module

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sampletask3.data.repository.ProductRepository
import com.example.sampletask3.ui.main.SharedViewModel
import com.example.sampletask3.utils.ViewModelProviderFactory
import com.example.sampletask3.utils.network.NetworkHelper
import com.example.sampletask3.utils.rx.SchedulerProvider
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

@Module
class ActivityModule(private val activity: AppCompatActivity) {

    @Provides
    fun provideActivity(): AppCompatActivity = activity

    @Provides
    fun provideLinearLayoutManager(): LinearLayoutManager = LinearLayoutManager(activity)

    @Provides
    fun provideMainSharedViewModel(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        productRepository: ProductRepository,
        networkHelper: NetworkHelper
    ): SharedViewModel = ViewModelProviders.of(
        activity,
        ViewModelProviderFactory(SharedViewModel::class) {
            SharedViewModel(
                schedulerProvider,
                compositeDisposable,
                productRepository,
                networkHelper
            )
        }).get(SharedViewModel::class.java)
}