package com.junho.imageapp.di

import android.app.Application
import androidx.room.Dao
import androidx.room.Room
import com.junho.imageapp.database.DataBaseWithRoom
import com.junho.imageapp.database.ImageDao
import com.junho.imageapp.database.MainDataBase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.koinApplication
import org.koin.dsl.module

val roomDBModule = module {

    fun provideDatabase(application: Application): MainDataBase {
        return Room.databaseBuilder(application, MainDataBase::class.java, "image_db")
            .fallbackToDestructiveMigration()
            .build()
    }

    fun provideMainDao(dataBase: MainDataBase): ImageDao {
        return dataBase.imageDao()
    }

    single{
        provideDatabase(androidApplication())
    }
    single { provideMainDao(get()) }
}
