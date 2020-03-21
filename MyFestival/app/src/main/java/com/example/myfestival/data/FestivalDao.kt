package com.example.myfestival.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myfestival.backend.Festival
import java.util.*

@Dao
interface FestivalDao {

    //TODO: dit is momenteel nog voor gewoon het eerste festival in de lijst op te vragen

    @Query("SELECT * FROM festivals LIMIT 1")
    fun getFestival(): LiveData<FestivalEntity>

    @Insert(entity = FestivalEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(festival: Festival)

}