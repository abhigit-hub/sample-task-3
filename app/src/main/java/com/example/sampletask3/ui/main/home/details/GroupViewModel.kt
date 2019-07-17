package com.example.sampletask3.ui.main.home.details

import com.example.sampletask3.data.remote.response.VariantGroupsItem
import com.example.sampletask3.di.ViewModelScope
import com.example.sampletask3.utils.network.NetworkHelper
import javax.inject.Inject

@ViewModelScope
class GroupViewModel @Inject constructor(
    private val networkHelper: NetworkHelper
) {

    private var currentItem: VariantGroupsItem? = null

    fun getGroupName() = currentItem?.name ?: ""

    fun updateData(variantGroupsItem: VariantGroupsItem) {
        currentItem = variantGroupsItem
    }
}