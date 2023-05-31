package com.fahad.hishabeetodo.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_table")
data class TodoEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: Long = 0,
    @ColumnInfo(name = "note_txt") val note_txt: String = "",
    @ColumnInfo(name = "is_checked") val is_checked: Boolean = false
)
