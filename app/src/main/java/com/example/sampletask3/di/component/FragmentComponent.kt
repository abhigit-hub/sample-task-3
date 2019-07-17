package com.example.sampletask3.di.component

import com.example.sampletask3.di.FragmentScope
import com.example.sampletask3.di.module.FragmentModule
import com.example.sampletask3.ui.main.home.HomeFragment
import dagger.Component

@FragmentScope
@Component(
    dependencies = [ApplicationComponent::class],
    modules = [FragmentModule::class]
)
interface FragmentComponent {

    fun inject(fragment: HomeFragment)
}