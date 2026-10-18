package com.raye.codeorbit_todo_list_app.data.local

import androidx.room.TypeConverter
import com.raye.codeorbit_todo_list_app.domain.model.Priority

class PriorityConverter {

    @TypeConverter
    fun fromPriority(priority: Priority): String = priority.name

    @TypeConverter
    fun toPriority(value: String): Priority = Priority.valueOf(value)
}