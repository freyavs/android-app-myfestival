package com.example.myfestival.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.myfestival.data.FestivalDao

@Database(entities = arrayOf(FestivalEntity::class), version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class FestivalDatabase : RoomDatabase() {
    abstract fun festivalDao(): FestivalDao

    companion object {

        @Volatile private var instance: FestivalDatabase? = null

        fun getInstance(context: Context) = instance ?:
                synchronized(this) {
                    instance ?: Room.databaseBuilder(
                        context.applicationContext, FestivalDatabase::class.java, "festival-db"
                    ).build().also { instance = it }
                }

    }
}