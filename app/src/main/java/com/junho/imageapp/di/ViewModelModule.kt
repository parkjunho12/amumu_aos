package com.junho.imageapp.di

import android.content.Context
import com.junho.imageapp.database.ImageDao
import com.junho.imageapp.repos.MainRepository
import com.junho.imageapp.repos.MainRepositoryImpl
import com.junho.imageapp.viewmodel.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val ViewModelModule = module {
    viewModel {
        MainViewModel(get())
    }
}

val dataModule = module {
    fun proviceMaindRepository(context: Context, imageDao: ImageDao) : MainRepository {
        return MainRepositoryImpl(context, imageDao)
    }

    factory <MainRepository>{
        proviceMaindRepository(androidContext(), get())
    }
}