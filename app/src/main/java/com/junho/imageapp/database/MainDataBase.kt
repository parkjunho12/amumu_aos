package com.junho.imageapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.junho.imageapp.database.format.ImageData

@Database(entities = [ImageData::class], version = 1)
abstract class MainDataBase : RoomDatabase(){
    abstract fun imageDao(): ImageDao
}