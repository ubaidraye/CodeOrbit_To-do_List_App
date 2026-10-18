package com.raye.codeorbit_todo_list_app.data.mapper

import com.raye.codeorbit_todo_list_app.data.local.dao.entity.TodoTaskEntity
import com.raye.codeorbit_todo_list_app.domain.model.TodoTask

fun TodoTaskEntity.toDomain(): TodoTask {
    return TodoTask(
        id = id,
        title = title,
        description = description,
        isCompleted = isCompleted,
        priority = priority,
        createdAt = createdAt
    )
}

fun TodoTask.toEntity(): TodoTaskEntity {
    return TodoTaskEntity(
        id = id,
        title = title,
        description = description,
        isCompleted = isCompleted,
        priority = priority,
        createdAt = createdAt
    )
}