package com.fahad.hishabeetodo.interfaces

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fahad.hishabeetodo.db.TodoEntity

@Dao
interface TodoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(channel: TodoEntity)

    @Query("UPDATE todo_table SET note_txt=:note_txt WHERE id = :id")
    fun updateText(note_txt: String, id: Long)

    @Query("UPDATE todo_table SET is_checked=:isChecked WHERE id = :id")
    fun updateBool(isChecked: Boolean, id: Long)

    @Query("SELECT * from todo_table ORDER BY is_checked ASC")
    fun getAll(): List<TodoEntity>

    @Query("DELETE from todo_table where id = :id")
    fun delete(id: Long)
}