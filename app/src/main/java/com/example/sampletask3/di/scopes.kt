package com.example.sampletask3.di

import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.BINARY)
annotation class ActivityScope

@Scope
@Retention(AnnotationRetention.BINARY)
annotation class FragmentScope

@Scope
@Retention(AnnotationRetention.BINARY)
annotation class ViewModelScope