package com.junho.imageapp.repos

import androidx.lifecycle.LiveData
import com.junho.imageapp.database.format.ImageData

interface BaseRepository {
    suspend fun getAllImageList(): List<ImageData>
}