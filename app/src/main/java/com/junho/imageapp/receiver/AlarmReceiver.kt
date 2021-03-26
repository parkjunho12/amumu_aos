package com.junho.imageapp.receiver

import android.content.*
import android.os.IBinder
import com.junho.imageapp.global.GlobalAppication
import com.junho.imageapp.service.ImageService

class AlarmReceiver: BroadcastReceiver() {
    private lateinit var mService: ImageService
    private val mainConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {

        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {

            val binder = service as ImageService.MainBinder
            mService = binder.getService()
            mService.refreshImage()
        }

    }
    override fun onReceive(context: Context?, intent: Intent?) {

        Intent(context, ImageService::class.java).also {
                intent -> context!!.applicationContext.bindService(intent, mainConnection, Context.BIND_AUTO_CREATE)
        }

    }
}