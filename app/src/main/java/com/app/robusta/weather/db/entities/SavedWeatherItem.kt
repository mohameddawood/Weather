package com.app.robusta.weather.db.entities

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer

@Entity(tableName = "HistoryWeatherItem")
class HistoryWeatherItem(
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    val photo: Bitmap,
    val time: String,
    val location: String,
    val temperature: String,
    val status: String,
    val icon: String,
) {
    @PrimaryKey(autoGenerate = true)
    var id = 0
}