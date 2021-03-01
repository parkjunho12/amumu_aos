package com.junho.imageapp.database

import android.content.Context
import androidx.room.Room

class DataBaseWithRoom(private val applicationContext: Context) {
    companion object {
        private const val IMAGE_DB_NAME = "image_db"
    }

    fun makeImageDatabase(): MainDataBase {
        return Room.databaseBuilder(applicationContext, MainDataBase::class.java, IMAGE_DB_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }
}