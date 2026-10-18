package com.raye.codeorbit_todo_list_app.domain.repository

import com.raye.codeorbit_todo_list_app.domain.model.TodoTask
import kotlinx.coroutines.flow.Flow

interface TodoRepository {
    fun getAllTasks(): Flow<List<TodoTask>>

    fun searchTasks(query: String): Flow<List<TodoTask>>

    fun getTaskById(id: Int): Flow<TodoTask?>

    suspend fun insertTask(task: TodoTask)

    suspend fun updateTask(task: TodoTask)

    suspend fun deleteTaskById(id: Int)
}