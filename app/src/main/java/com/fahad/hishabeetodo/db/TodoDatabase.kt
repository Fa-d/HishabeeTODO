package com.fahad.hishabeetodo.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fahad.hishabeetodo.interfaces.TodoDao

@Database(entities = [TodoEntity::class], version = 1, exportSchema = false)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun getTodoDao(): TodoDao

}
