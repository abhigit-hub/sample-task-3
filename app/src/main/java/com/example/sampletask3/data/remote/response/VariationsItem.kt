package com.example.sampletask3.data.remote.response

import com.google.gson.annotations.SerializedName

data class VariationsItem(

    @field:SerializedName("default")
    val jsonMemberDefault: Int? = null,

    @field:SerializedName("isVeg")
    val isVeg: Int? = null,

    @field:SerializedName("price")
    val price: Int = 0,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("inStock")
    val inStock: Int? = null,

    @field:SerializedName("id")
    val id: String? = null,

    var isSelected: Boolean = false,

    var combinationValid: Boolean = true
)