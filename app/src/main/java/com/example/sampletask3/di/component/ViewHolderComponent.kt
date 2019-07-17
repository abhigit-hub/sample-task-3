package com.example.sampletask3.di.component

import com.example.sampletask3.di.ViewModelScope
import com.example.sampletask3.di.module.ViewHolderModule
import com.example.sampletask3.ui.main.home.details.ChildViewHolder
import com.example.sampletask3.ui.main.home.details.GroupViewHolder
import dagger.Component

@ViewModelScope
@Component(
    dependencies = [ApplicationComponent::class],
    modules = [ViewHolderModule::class]
)
interface ViewHolderComponent {

    fun inject(viewholder: GroupViewHolder)

    fun inject(viewholder: ChildViewHolder)
}