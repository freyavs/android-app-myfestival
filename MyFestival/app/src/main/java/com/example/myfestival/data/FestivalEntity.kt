package com.example.myfestival.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "festivals")
data class FestivalEntity(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    val name: String,
    val location: String,
    val lastUpdated: Date
)

//TODO: foreign key toevoegen:

// https://stackoverflow.com/questions/45988446/how-to-create-a-table-with-a-two-or-more-foreign-keys-using-android-room
// https://stackoverflow.com/questions/45457603/storing-a-model-object-with-lists-in-room-database
// https://medium.com/@kirillsuslov/how-to-add-more-that-one-entity-in-room-5cc3743219c0