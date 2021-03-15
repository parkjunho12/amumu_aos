package com.junho.imageapp.common

import androidx.lifecycle.MutableLiveData
import com.junho.imageapp.database.format.ImageData
import com.junho.imageapp.repos.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CustomListLiveData(arrayList: ArrayList<ImageData>): MutableLiveData<ArrayList<ImageData>>() {
    private var imageArrayList = arrayList

    init {
        value = imageArrayList
    }

    suspend fun fetchData(repository: MainRepository) {
        imageArrayList = (repository.getAllImageList() as ArrayList<ImageData>?)!!
        withContext(Dispatchers.Main) {
            value = imageArrayList
        }
    }

    suspend fun addImage(repository: MainRepository, imageData: ImageData) {
        repository.insertImage(imageData)
        imageArrayList.add(imageData)
        withContext(Dispatchers.Main) {
            value = imageArrayList
        }
    }

    suspend fun deleteImage(repository: MainRepository, imageData: ImageData) {
        repository.deleteImage(imageData)
        imageArrayList.remove(imageData)
        withContext(Dispatchers.Main) {
            value = imageArrayList
        }
    }
}