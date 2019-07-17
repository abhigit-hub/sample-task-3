package com.example.sampletask3.data.remote.response

import com.google.gson.annotations.SerializedName

data class Variants(

    @field:SerializedName("variant_groups")
    val variantGroups: List<VariantGroupsItem>? = null,

    @field:SerializedName("exclude_list")
    val excludeList: List<List<ExcludeListItemItem>?>? = null
)