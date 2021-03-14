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

    fun setArrayList() {

    }

    suspend fun fetchData(repository: MainRepository) {
        withContext(Dispatchers.Main) {
            value = repository.getAllImageList() as ArrayList<ImageData>?
        }
    }

    suspend fun addImage(repository: MainRepository, imageData: ImageData) {
        repository.insertImage(imageData)
        fetchData(repository)
    }

    suspend fun deleteImage(repository: MainRepository, imageData: ImageData) {
        repository.deleteImage(imageData)
        fetchData(repository)
    }
}