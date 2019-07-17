package com.example.sampletask3.di.module

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sampletask3.data.repository.ProductRepository
import com.example.sampletask3.ui.main.SharedViewModel
import com.example.sampletask3.ui.main.home.details.GroupAdapter
import com.example.sampletask3.utils.ViewModelProviderFactory
import com.example.sampletask3.utils.network.NetworkHelper
import com.example.sampletask3.utils.rx.SchedulerProvider
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

@Module
class FragmentModule(private val fragment: Fragment) {

    @Provides
    fun provideFragment(): Fragment = fragment

    @Provides
    fun provideLinearLayoutManager(): LinearLayoutManager = LinearLayoutManager(fragment.context)

    @Provides
    fun provideMainViewModel(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        productRepository: ProductRepository,
        networkHelper: NetworkHelper
    ): SharedViewModel =
        ViewModelProviders.of(fragment.requireActivity(),
            ViewModelProviderFactory(SharedViewModel::class) {
                SharedViewModel(
                    schedulerProvider,
                    compositeDisposable,
                    productRepository,
                    networkHelper
                )
            }).get(SharedViewModel::class.java)

    @Provides
    fun provideGroupAdapter() = GroupAdapter(mutableListOf())
}