package com.example.sampletask3.data.remote.response

import com.google.gson.annotations.SerializedName

data class ExcludeListItemItem(

    @field:SerializedName("group_id")
    val groupId: String = "",

    @field:SerializedName("variation_id")
    val variationId: String = ""
)