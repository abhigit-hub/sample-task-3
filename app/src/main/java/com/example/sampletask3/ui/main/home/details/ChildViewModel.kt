package com.example.sampletask3.ui.main.home.details

import com.example.sampletask3.R
import com.example.sampletask3.data.remote.response.VariationsItem
import com.example.sampletask3.di.ViewModelScope
import com.example.sampletask3.utils.network.NetworkHelper
import javax.inject.Inject

@ViewModelScope
class ChildViewModel @Inject constructor(
    private val networkHelper: NetworkHelper
) {

    private var currentItem: VariationsItem? = null
    lateinit var selectionUpdateEvent: (String) -> Unit
    lateinit var messageIdListener: (Int) -> Unit
    lateinit var messageListener: (String) -> Unit

    fun getVariationName() = currentItem?.name ?: ""
    fun getPrice() = currentItem?.price ?: 0
    fun getIsSelected() = currentItem?.isSelected ?: false
    fun getIsCombinationValid() = currentItem?.combinationValid ?: true
    fun getStockCount() = currentItem?.inStock ?: 0

    fun updateData(variationsItem: VariationsItem) {
        currentItem = variationsItem
    }

    fun onSelected() {
        currentItem?.id?.let {
            if (getStockCount() > 0) {
                if (getIsCombinationValid())
                    selectionUpdateEvent(it)
                else messageIdListener(R.string.invalid_combination)
            } else messageIdListener(R.string.cannot_select_out_of_stock)
        }
    }
}