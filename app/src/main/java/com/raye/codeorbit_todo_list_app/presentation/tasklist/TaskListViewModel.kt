package com.raye.codeorbit_todo_list_app.presentation.tasklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raye.codeorbit_todo_list_app.domain.model.Priority
import com.raye.codeorbit_todo_list_app.domain.model.TaskSortOrder
import com.raye.codeorbit_todo_list_app.domain.model.TodoTask
import com.raye.codeorbit_todo_list_app.domain.repository.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val repository: TodoRepository
) : ViewModel() {

    private val _sortOrder = MutableStateFlow(TaskSortOrder.NEWEST_FIRST)
    val sortOrder = _sortOrder.asStateFlow()

    val uiState: StateFlow<TaskListUiState> = combine(
        repository.getAllTasks(),
        _sortOrder
    ) { tasks, sortOrder ->
        val sortedTasks = sortTasks(tasks, sortOrder)
        TaskListUiState.Success(sortedTasks) as TaskListUiState
    }.catch { throwable ->
        emit(TaskListUiState.Error(throwable.localizedMessage ?: "Failed to load tasks"))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = TaskListUiState.Loading
    )

    fun changeSortOrder(sortOrder: TaskSortOrder) {
        _sortOrder.value = sortOrder
    }

    fun sortTasks(
        tasks: List<TodoTask>,
        sortOrder: TaskSortOrder
    ): List<TodoTask> {
        val pendingTasks = tasks.filterNot { it.isCompleted }
        val completedTasks = tasks.filter { it.isCompleted }

        fun sortGroup(group: List<TodoTask>): List<TodoTask> {
            return when (sortOrder) {
                TaskSortOrder.NEWEST_FIRST ->
                    group.sortedByDescending { it.createdAt }

                TaskSortOrder.OLDEST_FIRST ->
                    group.sortedBy { it.createdAt }

                TaskSortOrder.PRIORITY ->
                    group.sortedBy { it.priority.ordinal * -1 }
            }
        }

        return sortGroup(pendingTasks) + sortGroup(completedTasks)
    }

    fun addTask(
        title: String,
        description: String,
        priority: Priority,
        isCompleted: Boolean
    ) {
        viewModelScope.launch {
            if (title.isNotEmpty()) {
                repository.insertTask(
                    TodoTask(
                        title = title,
                        description = description,
                        priority = priority,
                        isCompleted = isCompleted
                    )
                )
            }
        }
    }

    fun updateTask(
        task: TodoTask,
        isCompleted: Boolean
    ) {
        viewModelScope.launch {
            repository.updateTask(
                task.copy(isCompleted = isCompleted)
            )
        }
    }

    fun editTask(
        task: TodoTask,
        title: String,
        description: String,
        priority: Priority,
        isCompleted: Boolean
    ) {
        viewModelScope.launch {
            repository.updateTask(
                task.copy(
                    title = title,
                    description = description,
                    priority = priority,
                    isCompleted = isCompleted
                )
            )
        }
    }

    fun deleteTask(taskId: Int) {
        viewModelScope.launch {
            repository.deleteTaskById(taskId)
        }
    }
}