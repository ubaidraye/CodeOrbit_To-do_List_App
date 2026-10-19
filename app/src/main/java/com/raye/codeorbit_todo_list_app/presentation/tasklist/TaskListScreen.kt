package com.raye.codeorbit_todo_list_app.presentation.tasklist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.raye.codeorbit_todo_list_app.domain.model.TodoTask
import com.raye.codeorbit_todo_list_app.presentation.tasklist.components.AddEditTaskSheet
import com.raye.codeorbit_todo_list_app.presentation.tasklist.components.EmptyTasksContent
import com.raye.codeorbit_todo_list_app.presentation.tasklist.components.ErrorContent
import com.raye.codeorbit_todo_list_app.presentation.tasklist.components.LoadingContent
import com.raye.codeorbit_todo_list_app.presentation.tasklist.components.TaskListContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    modifier: Modifier = Modifier,
    viewModel: TaskListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val sortOrder by viewModel.sortOrder.collectAsStateWithLifecycle()

    var isSheetOpen by rememberSaveable { mutableStateOf(false) }

    var taskToEdit by remember {
        mutableStateOf<TodoTask?>(null)
    }


    AddEditTaskSheet(
        onDismissRequest = {
            isSheetOpen = false
            taskToEdit = null
        },
        isSheetOpen = isSheetOpen,
        taskToEdit = taskToEdit,
        onSave = { title, description, priority, isCompleted ->
            val existingTask = taskToEdit

            if (existingTask == null) {
                viewModel.addTask(
                    title = title,
                    description = description,
                    priority = priority,
                    isCompleted = isCompleted
                )
            } else {
                viewModel.editTask(
                    task = existingTask,
                    title = title,
                    description = description,
                    priority = priority,
                    isCompleted = isCompleted
                )
            }

            isSheetOpen = false
            taskToEdit = null
        }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CodeOrbit To-Do Task") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    taskToEdit = null
                    isSheetOpen = true
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
            }
        }
    ) { innerPadding ->

        Box(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {

            when (val state = uiState) {
                is TaskListUiState.Loading -> {

                    LoadingContent(modifier = modifier)
                }

                is TaskListUiState.Error -> {

                    ErrorContent(message = state.message, modifier = modifier)
                }

                is TaskListUiState.Success -> {

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {

                        if (state.tasks.isEmpty()) {
                            EmptyTasksContent(modifier = modifier)

                        } else {
                            TaskListContent(
                                tasks = state.tasks,
                                sortOrder = sortOrder,
                                onSortOrderChange = viewModel::changeSortOrder,
                                onTaskClick = { task ->
                                    taskToEdit = task
                                    isSheetOpen = true
                                },
                                onTaskToggle = { task, isCompleted ->
                                    viewModel.updateTask(task, isCompleted)
                                },
                                onDeleteClick = viewModel::deleteTask
                            )
                        }
                    }
                }
            }
        }
    }
}