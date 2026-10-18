package com.raye.codeorbit_todo_list_app.data.local.dao.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.raye.codeorbit_todo_list_app.domain.model.Priority

@Entity(tableName = "todo_tasks")
data class TodoTaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String = "",
    val isCompleted: Boolean = false,
    val priority: Priority = Priority.MEDIUM,
    val createdAt: Long = System.currentTimeMillis()
)
