package com.example.sampletask3.di.component

import com.example.sampletask3.di.ActivityScope
import com.example.sampletask3.di.module.ActivityModule
import com.example.sampletask3.ui.main.MainActivity
import dagger.Component

@ActivityScope
@Component(
    dependencies = [ApplicationComponent::class],
    modules = [ActivityModule::class]
)
interface ActivityComponent {

    fun inject(activity: MainActivity)
}