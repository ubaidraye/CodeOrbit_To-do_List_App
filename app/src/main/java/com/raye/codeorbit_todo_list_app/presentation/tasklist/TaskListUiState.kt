package com.raye.codeorbit_todo_list_app.presentation.tasklist

import com.raye.codeorbit_todo_list_app.domain.model.TodoTask

sealed interface TaskListUiState {

    data object Loading : TaskListUiState

    data class Success(
        val tasks: List<TodoTask>
    ) : TaskListUiState

    data class Error(val message: String) : TaskListUiState
}