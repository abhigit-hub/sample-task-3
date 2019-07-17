package com.example.sampletask3.utils.binding

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.sampletask3.R

@BindingAdapter("app:formatPrice")
fun formatPrice(view: TextView, value: Int) {
    if (value > 0) {
        val formattedText = "$ $value"
        view.text = formattedText
    }
}


@BindingAdapter("app:formatStockCount")
fun formatStockCount(view: TextView, stockCount: Int) {
    if (stockCount > 0) {
        view.text = view.context.getString(R.string.in_stock)
        view.setTextColor(view.context.resources.getColor(R.color.dark_green))
    } else {
        view.text = view.context.getString(R.string.out_of_stock)
        view.setTextColor(view.context.resources.getColor(R.color.red))
    }
}

@BindingAdapter("app:formatCombinationAlert")
fun formatCombinationAlert(view: TextView, combinationValid: Boolean) {
    if (!combinationValid) {
        view.text = view.context.getString(R.string.invalid_combination)
        view.setTextColor(view.context.resources.getColor(R.color.red))
    }
}