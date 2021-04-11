package com.junho.imageapp.global

import android.app.Application
import com.junho.imageapp.di.ViewModelModule
import com.junho.imageapp.di.dataModule
import com.junho.imageapp.di.roomDBModule
import com.junho.imageapp.service.ImageService
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.component.KoinApiExtension
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.koinApplication

class GlobalAppication: Application() {


    @KoinApiExtension
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@GlobalAppication)
            androidFileProperties()
            modules(listOf(dataModule, roomDBModule, ViewModelModule))

        }
        imageService = ImageService()
    }

    override fun onTerminate() {

        super.onTerminate()
    }

    companion object {
        lateinit var imageService: ImageService
    }
}