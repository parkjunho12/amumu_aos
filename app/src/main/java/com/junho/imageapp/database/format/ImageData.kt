package com.junho.imageapp.database.format

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "Images")
data class ImageData(
    @PrimaryKey(autoGenerate = true) val index: Int = 0,
    @ColumnInfo val imageUri: String
) : Serializable