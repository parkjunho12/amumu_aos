package com.junho.imageapp.global

import android.app.Application
import android.content.Intent
import android.content.IntentFilter
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.junho.imageapp.di.ViewModelModule
import com.junho.imageapp.di.dataModule
import com.junho.imageapp.di.roomDBModule
import com.junho.imageapp.receiver.RebootReceiver
import com.junho.imageapp.service.ImageService
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.component.KoinApiExtension
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.koinApplication

class GlobalAppication: Application() {

    val bootReceiver = RebootReceiver()
    val filter = IntentFilter(Intent.ACTION_BOOT_COMPLETED)
    private lateinit var firebaseAnalytics: FirebaseAnalytics


    @KoinApiExtension
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@GlobalAppication)
            androidFileProperties()
            modules(listOf(dataModule, roomDBModule, ViewModelModule))

        }
        prefs = Prefs(applicationContext)
        imageService = ImageService()
        registerReceiver(bootReceiver, filter)
        firebaseAnalytics = Firebase.analytics
    }

    override fun onTerminate() {
        unregisterReceiver(bootReceiver)
        super.onTerminate()
    }

    companion object {
        @Volatile
        lateinit var prefs: Prefs
        lateinit var imageService: ImageService
    }
}