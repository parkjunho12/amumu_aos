package com.junho.imageapp.di

import com.junho.imageapp.repos.MainRepository
import com.junho.imageapp.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val ViewModelModule = module {
    viewModel {
        MainViewModel(get())
    }
}

val dataModule = module {
    factory <MainRepository>{
        MainRepository()
    }
}