package com.junho.imageapp.repos

import android.content.Context
import androidx.lifecycle.LiveData
import com.junho.imageapp.database.DataBaseWithRoom
import com.junho.imageapp.database.ImageDao
import com.junho.imageapp.database.MainDataBase
import com.junho.imageapp.database.format.ImageData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.java.KoinJavaComponent



interface MainRepository : BaseRepository{
    override suspend fun getAllImageList(): List<ImageData>

    suspend fun insertImage(imageData: ImageData)
}

class MainRepositoryImpl(private val context: Context, private val imageDao: ImageDao):
        MainRepository {
    override suspend fun getAllImageList(): List<ImageData>{
        return withContext(Dispatchers.IO) {
            imageDao.getAll()
        }
    }

    override suspend fun insertImage(imageData: ImageData) {
        imageDao.insertAll(imageData)
    }

}


