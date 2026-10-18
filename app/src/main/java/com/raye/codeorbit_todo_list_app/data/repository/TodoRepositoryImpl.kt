package com.raye.codeorbit_todo_list_app.data.repository

import com.raye.codeorbit_todo_list_app.data.local.dao.TodoDao
import com.raye.codeorbit_todo_list_app.data.mapper.toDomain
import com.raye.codeorbit_todo_list_app.data.mapper.toEntity
import com.raye.codeorbit_todo_list_app.domain.model.TodoTask
import com.raye.codeorbit_todo_list_app.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TodoRepositoryImpl @Inject constructor(
    private val todoDao: TodoDao
) : TodoRepository {
    override fun getAllTasks(): Flow<List<TodoTask>> =
        todoDao.getAllTasks().map { tasks ->
            tasks.map { it.toDomain() }
        }


    override fun searchTasks(query: String): Flow<List<TodoTask>> =
        todoDao.searchTasks(query = query).map { tasks ->
            tasks.map { it.toDomain() }
        }


    override fun getTaskById(id: Int): Flow<TodoTask?> {
        return todoDao.getTaskById(id = id).map { it?.toDomain() }
    }

    override suspend fun insertTask(task: TodoTask) {
        todoDao.insertTask(task.toEntity())
    }

    override suspend fun updateTask(task: TodoTask) {
        todoDao.updateTask(task.toEntity())
    }

    override suspend fun deleteTaskById(id: Int) {
        todoDao.deleteTaskById(id)
    }
}