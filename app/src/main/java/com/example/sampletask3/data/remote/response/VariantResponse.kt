package com.example.sampletask3.data.remote.response

import com.google.gson.annotations.SerializedName

data class VariantResponse(

    @field:SerializedName("variants")
    val variants: Variants? = null
)