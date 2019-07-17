package com.example.sampletask3.data.repository

import com.example.sampletask3.data.local.prefs.UserPreferences
import com.example.sampletask3.data.remote.NetworkService
import com.example.sampletask3.data.remote.response.VariantResponse
import com.example.sampletask3.utils.network.NetworkHelper
import com.example.sampletask3.utils.rx.SchedulerProvider
import io.reactivex.Single
import javax.inject.Singleton

@Singleton
class ProductRepository constructor(
    private val networkService: NetworkService,
    private val schedulerProvider: SchedulerProvider,
    private val userPreferences: UserPreferences,
    private val networkHelper: NetworkHelper
) {

    companion object {
        const val TAG = "ProductRepository"
    }

    fun getProductDetail(): Single<VariantResponse> {
        return networkService.doGetProductDetailApiCall()
    }
}
