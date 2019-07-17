package com.example.sampletask3.data.remote

import com.example.sampletask3.data.remote.response.VariantResponse
import io.reactivex.Single
import retrofit2.http.GET
import javax.inject.Singleton

@Singleton
interface NetworkService {

    @GET(Endpoints.PRODUCT_VARIATION)
    fun doGetProductDetailApiCall(): Single<VariantResponse>
}