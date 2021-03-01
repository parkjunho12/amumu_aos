package com.junho.imageapp.database.format

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Images")
data class ImageData(
    @PrimaryKey(autoGenerate = true) val idx: Int,
    @ColumnInfo val imageUri: String
)