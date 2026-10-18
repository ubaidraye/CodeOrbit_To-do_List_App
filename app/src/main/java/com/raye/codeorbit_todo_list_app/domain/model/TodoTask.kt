package com.raye.codeorbit_todo_list_app.domain.model

data class TodoTask(
    val id: Int,
    val title: String,
    val description: String,
    val isCompleted: Boolean,
    val priority: Priority,
    val createdAt: Long
)
