package com.example.myfestival.data

import androidx.room.TypeConverter
import java.util.*

class Converters {

    @TypeConverter
    fun toTimestamp(date: Date): Long = date.time

    @TypeConverter
    fun fromTimestamp(value: Long): Date = Date(value)

}