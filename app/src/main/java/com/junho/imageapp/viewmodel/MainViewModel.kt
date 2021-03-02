package com.junho.imageapp.viewmodel

import androidx.lifecycle.*
import com.junho.imageapp.database.format.ImageData
import com.junho.imageapp.repos.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent


@KoinApiExtension
class MainViewModel(private val repository: MainRepository) : BaseViewModel(), KoinComponent{

    private val _imageDataList = MutableLiveData<ArrayList<ImageData>>()
    val imageDataList: LiveData<ArrayList<ImageData>>
        get() = _imageDataList

    fun fetchDataList() {
        viewModelScope.launch(Dispatchers.IO) { _imageDataList.postValue(repository.getAllImageList() as ArrayList<ImageData>?)
        }
    }

    fun insertImageData(imageData: ImageData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertImage(imageData)
            _imageDataList.postValue(repository.getAllImageList() as ArrayList<ImageData>?)
        }
    }


    override fun onCleared() {
        super.onCleared()
    }
}