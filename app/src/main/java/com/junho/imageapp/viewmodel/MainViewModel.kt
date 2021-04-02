package com.junho.imageapp.viewmodel

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.*
import com.junho.imageapp.common.CustomListLiveData
import com.junho.imageapp.database.format.ImageData
import com.junho.imageapp.repos.MainRepository
import com.junho.imageapp.service.ImageService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent


@KoinApiExtension
class MainViewModel(private val repository: MainRepository) : BaseViewModel(), KoinComponent{

    private val _imageDataList = CustomListLiveData(ArrayList<ImageData>())
    val imageDataList: LiveData<ArrayList<ImageData>>
        get() = _imageDataList

    fun fetchDataList() {
        viewModelScope.launch {
            _imageDataList.fetchData(repository)
        }
    }

    fun insertImageData(imageData: ImageData) {
        viewModelScope.launch(Dispatchers.IO) {
            _imageDataList.addImage(repository, imageData)
        }
    }


    override fun onCleared() {
        super.onCleared()
    }

    fun deleteItem(imageData: ImageData) {
        viewModelScope.launch(Dispatchers.IO) {
            _imageDataList.deleteImage(repository, imageData)
        }
    }
}