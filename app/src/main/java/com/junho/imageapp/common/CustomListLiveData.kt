package com.junho.imageapp.common

import androidx.lifecycle.MutableLiveData
import com.junho.imageapp.database.format.ImageData

class CustomListLiveData(arrayList: ArrayList<ImageData>): MutableLiveData<ArrayList<ImageData>>() {
    private var imageArrayList = arrayList

    init {
        value = imageArrayList
    }

    fun setArrayList() {
        
    }
}