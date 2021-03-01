package com.junho.imageapp.di

import com.junho.imageapp.database.DataBaseWithRoom
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val roomDBModule = module {
    single {
        DataBaseWithRoom(androidApplication().applicationContext).makeImageDatabase()
    }
}