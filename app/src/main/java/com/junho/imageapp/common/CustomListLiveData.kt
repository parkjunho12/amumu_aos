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
            imageArrayList = (repository.getAllImageList() as ArrayList<ImageData>?)!!
            value = imageArrayList
        }
    }

    suspend fun addImage(repository: MainRepository, imageData: ImageData) {
        repository.insertImage(imageData)
        imageArrayList.add(imageData)
        value = imageArrayList
    }

    suspend fun deleteImage(repository: MainRepository, imageData: ImageData) {
        repository.deleteImage(imageData)
        imageArrayList.remove(imageData)
        value = imageArrayList
    }
}