package com.example.sampletask3.ui.base

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

    internal var callback: ViewHolderCallback? = null

    open fun onBind(any: Any) {
        bindViewModel(any)
    }

    protected abstract fun bindViewModel(data: Any)

    fun setCallback(callback: ViewHolderCallback) {
        this.callback = callback
    }

    fun removeCallback() {
        this.callback = null
    }

    interface ViewHolderCallback
}