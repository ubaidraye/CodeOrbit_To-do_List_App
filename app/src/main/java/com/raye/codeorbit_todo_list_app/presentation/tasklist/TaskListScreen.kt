package com.raye.codeorbit_todo_list_app.presentation.tasklist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.raye.codeorbit_todo_list_app.domain.model.TaskSortOrder
import com.raye.codeorbit_todo_list_app.domain.model.TodoTask
import com.raye.codeorbit_todo_list_app.presentation.tasklist.components.AddEditTaskSheet
import com.raye.codeorbit_todo_list_app.presentation.tasklist.components.TaskItem

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
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                        Text("Loading ...")
                    }
                }

                is TaskListUiState.Error -> {

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = state.message,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                is TaskListUiState.Success -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {

                        if (state.tasks.isEmpty()) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text("No tasks, add one by clicking +")
                            }

                        } else {
                            var sortMenuExpanded by remember { mutableStateOf(false) }

                            Box {
                                TextButton(onClick = { sortMenuExpanded = true }) {
                                    Text(
                                        when (sortOrder) {
                                            TaskSortOrder.NEWEST_FIRST -> "Newest first"
                                            TaskSortOrder.OLDEST_FIRST -> "Oldest first"
                                            TaskSortOrder.PRIORITY -> "Priority"
                                        }
                                    )
                                    Icon(
                                        imageVector = Icons.Default.ArrowDropDown,
                                        contentDescription = "Choose sort order"
                                    )
                                }

                                DropdownMenu(
                                    expanded = sortMenuExpanded,
                                    onDismissRequest = { sortMenuExpanded = false }
                                ) {
                                    TaskSortOrder.entries.forEach { order ->
                                        DropdownMenuItem(
                                            text = {
                                                Text(
                                                    when (order) {
                                                        TaskSortOrder.NEWEST_FIRST -> "Newest first"
                                                        TaskSortOrder.OLDEST_FIRST -> "Oldest first"
                                                        TaskSortOrder.PRIORITY -> "Priority"
                                                    }
                                                )
                                            },
                                            onClick = {
                                                viewModel.changeSortOrder(order)
                                                sortMenuExpanded = false
                                            }
                                        )
                                    }
                                }
                            }

                            val sortedTasks = viewModel.sortTasks(state.tasks, sortOrder)

                            LazyColumn(
                                verticalArrangement = Arrangement.spacedBy(4.dp),
                                contentPadding = PaddingValues(horizontal = 8.dp)
                            ) {
                                items(
                                    items = sortedTasks,
                                    key = { it.id },
                                    contentType = { "task_item" }
                                ) { task ->
                                    TaskItem(
                                        modifier = Modifier.animateItem(),
                                        task = task,
                                        onTaskClick = {
                                            taskToEdit = task
                                            isSheetOpen = true
                                        },
                                        onTaskToggle = { _, isCompleted ->
                                            viewModel.updateTask(
                                                task = task,
                                                isCompleted = isCompleted
                                            )
                                        },
                                        onDeleteClick = { taskId ->
                                            viewModel.deleteTask(taskId)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}