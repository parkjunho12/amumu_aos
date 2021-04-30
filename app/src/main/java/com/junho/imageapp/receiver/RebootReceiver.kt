package com.junho.imageapp.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.junho.imageapp.global.GlobalAppication
import com.junho.imageapp.service.ImageService

class RebootReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent != null && intent.action != null && intent.action == "android.intent.action.BOOT_COMPLETED") {
            if (GlobalAppication.prefs.isSwitchOn) {
                val imageservice = Intent(context!!, GlobalAppication.imageService::class.java)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (!ImageService.isInit) {
                        context.startForegroundService(imageservice)
                    }
                } else {
                    if (!ImageService.isInit) {
                        context.startService(imageservice)
                    }
                }
            }
        }
    }
}