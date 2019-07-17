package com.example.sampletask3.data.remote.response

import com.google.gson.annotations.SerializedName

data class VariantGroupsItem(

    @field:SerializedName("group_id")
    val groupId: String = "",

    @field:SerializedName("variations")
    val variations: List<VariationsItem>? = null,

    @field:SerializedName("name")
    val name: String? = null
)