package com.example.sampletask3

import android.app.Application
import com.example.sampletask3.di.component.ApplicationComponent
import com.example.sampletask3.di.component.DaggerApplicationComponent
import com.example.sampletask3.di.module.ApplicationModule

class MyApp : Application() {

    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        injectDependencies()
    }

    private fun injectDependencies() {
        applicationComponent = DaggerApplicationComponent
            .builder()
            .applicationModule(ApplicationModule(this))
            .build()
        applicationComponent.inject(this)
    }
}