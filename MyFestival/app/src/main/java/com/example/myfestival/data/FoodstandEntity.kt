package com.example.myfestival.data
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "foodstands")
data class FoodstandEntity(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    val name: String,
    //TODO: menu moet eig wss ook in apparte entity
    val menu: String,
    val lastUpdated: Date
)