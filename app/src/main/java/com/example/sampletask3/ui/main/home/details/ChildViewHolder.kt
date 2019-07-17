package com.example.sampletask3.ui.main.home.details

import com.example.sampletask3.MyApp
import com.example.sampletask3.data.remote.response.VariationsItem
import com.example.sampletask3.databinding.ItemChildBinding
import com.example.sampletask3.di.component.DaggerViewHolderComponent
import com.example.sampletask3.di.module.ViewHolderModule
import com.example.sampletask3.ui.base.BaseViewHolder
import com.example.sampletask3.utils.display.Toaster
import javax.inject.Inject

class ChildViewHolder(private val binding: ItemChildBinding) : BaseViewHolder(binding) {

    @Inject
    lateinit var viewModel: ChildViewModel

    init {
        DaggerViewHolderComponent.builder()
            .applicationComponent((binding.root.context.applicationContext as MyApp).applicationComponent)
            .viewHolderModule(ViewHolderModule(this))
            .build()
            .inject(this)

        viewModel.messageIdListener = {
            Toaster.show(
                binding.root.context,
                binding.root.context.getString(it)
            )
        }

        viewModel.messageListener = {
            Toaster.show(
                binding.root.context,
                it
            )
        }

        viewModel.selectionUpdateEvent = {
            callback?.let { viewHolderCallback ->
                (viewHolderCallback as ChildViewHolderCallback).onItemSelected(it)
            }
        }
    }

    override fun bindViewModel(data: Any) {
        (data as VariationsItem).run {
            viewModel.updateData(this)
            binding.viewModel = viewModel
            binding.executePendingBindings()
        }
    }

    interface ChildViewHolderCallback : ViewHolderCallback {
        fun onItemSelected(variationId: String)
    }
}