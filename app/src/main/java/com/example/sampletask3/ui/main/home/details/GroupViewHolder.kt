package com.example.sampletask3.ui.main.home.details

import com.example.sampletask3.MyApp
import com.example.sampletask3.data.remote.response.VariantGroupsItem
import com.example.sampletask3.databinding.ItemGroupBinding
import com.example.sampletask3.di.component.DaggerViewHolderComponent
import com.example.sampletask3.di.module.ViewHolderModule
import com.example.sampletask3.ui.base.BaseViewHolder
import javax.inject.Inject

class GroupViewHolder(private val binding: ItemGroupBinding) : BaseViewHolder(binding) {

    @Inject
    lateinit var viewModel: GroupViewModel

    init {
        DaggerViewHolderComponent.builder()
            .applicationComponent((binding.root.context.applicationContext as MyApp).applicationComponent)
            .viewHolderModule(ViewHolderModule(this))
            .build()
            .inject(this)
    }

    override fun bindViewModel(data: Any) {
        (data as VariantGroupsItem).run {
            viewModel.updateData(this)
            binding.viewModel = viewModel
            binding.executePendingBindings()
        }
    }
}