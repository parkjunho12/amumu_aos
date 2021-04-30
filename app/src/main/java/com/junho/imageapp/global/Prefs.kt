package com.junho.imageapp.global

import android.content.Context
import android.content.SharedPreferences

class Prefs(context: Context) {
    val prefs: SharedPreferences = context.getSharedPreferences(
        "PREFS_FILENAME",
        Context.MODE_PRIVATE
    )

    var isSwitchOn: Boolean
        get() = prefs.getBoolean("isSwitchOn", false)
        set(value) = prefs.edit().putBoolean("isSwitchOn", value).apply()
}