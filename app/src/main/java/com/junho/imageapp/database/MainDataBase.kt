package com.junho.imageapp.database

import android.content.Context
import androidx.databinding.adapters.Converters
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.junho.imageapp.database.format.ImageData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [ImageData::class], version = 1)
abstract class MainDataBase : RoomDatabase(){
    abstract fun imageDao(): ImageDao

    companion object {
        private const val IMAGE_DB_NAME = "image_db"

        fun getInstance(context: Context) : MainDataBase {
            return buildDatabase(context)
        }

        private fun buildDatabase(context: Context): MainDataBase {
            return Room.databaseBuilder(context, MainDataBase::class.java, IMAGE_DB_NAME)
                .addCallback(object: RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        CoroutineScope(Dispatchers.IO).launch {
                            println(db)
                        }
                    }
                })
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}