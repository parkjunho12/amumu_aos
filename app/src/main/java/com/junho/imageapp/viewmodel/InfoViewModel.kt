package com.junho.imageapp.viewmodel

import androidx.lifecycle.viewModelScope
import com.junho.imageapp.database.format.ImageData
import com.junho.imageapp.repos.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class InfoViewModel(private val repository: MainRepository) : BaseViewModel(), KoinComponent {

    fun deleteItem(imageData: ImageData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteImage(imageData)
        }
    }
}