package com.junho.imageapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.junho.imageapp.database.format.ImageData

@Dao
interface ImageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg image: ImageData)

    @Query("SELECT * FROM Images")
    fun getAll(): List<ImageData>

    @Update
    fun update(image: ImageData)

    @Delete
    fun delete(image: ImageData)
}