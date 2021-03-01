package com.junho.imageapp.viewmodel

import androidx.lifecycle.*
import com.junho.imageapp.database.DataBaseWithRoom
import com.junho.imageapp.database.MainDataBase
import com.junho.imageapp.database.format.ImageData
import com.junho.imageapp.repos.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


@KoinApiExtension
class MainViewModel(private val repository: MainRepository) : BaseViewModel(), KoinComponent{

    private val _imageDataList = MutableLiveData<ArrayList<ImageData>>()
    val imageDataList: LiveData<ArrayList<ImageData>>
        get() = _imageDataList

    fun fetchDataList() {
        viewModelScope.launch(Dispatchers.IO) {
            _imageDataList.value?.add(ImageData(0, ""))
            repository.loadImageDatas().forEach {
                imageData -> _imageDataList.value?.add(imageData)
            }
        }
    }


    override fun onCleared() {
        super.onCleared()
    }
}