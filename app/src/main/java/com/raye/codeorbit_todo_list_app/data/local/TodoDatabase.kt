package com.raye.codeorbit_todo_list_app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.raye.codeorbit_todo_list_app.data.local.dao.TodoDao
import com.raye.codeorbit_todo_list_app.data.local.dao.entity.TodoTaskEntity

@TypeConverters(PriorityConverter::class)
@Database(entities = [TodoTaskEntity::class], version = 1, exportSchema = false)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
}

