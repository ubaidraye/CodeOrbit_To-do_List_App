package com.raye.codeorbit_todo_list_app.presentation.tasklist.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.raye.codeorbit_todo_list_app.domain.model.TaskSortOrder
import com.raye.codeorbit_todo_list_app.domain.model.TodoTask

@Composable
fun TaskListContent(
    tasks: List<TodoTask>,
    sortOrder: TaskSortOrder,
    onSortOrderChange: (TaskSortOrder) -> Unit,
    onTaskClick: (TodoTask) -> Unit,
    onTaskToggle: (TodoTask, Boolean) -> Unit,
    onDeleteClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var sortMenuExpanded by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxSize()) {
        Box(modifier = Modifier.padding(horizontal = 8.dp)) {
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
                            onSortOrderChange(order)
                            sortMenuExpanded = false
                        }
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier.weight(1f), // 2. Add weight(1f) to take remaining height
            verticalArrangement = Arrangement.spacedBy(4.dp),
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            items(
                items = tasks,
                key = { it.id },
                contentType = { "task_item" }
            ) { task ->
                TaskItem(
                    modifier = Modifier.animateItem(),
                    task = task,
                    onTaskClick = { onTaskClick(task) },
                    onTaskToggle = { _, isCompleted ->
                        onTaskToggle(task, isCompleted)
                    },
                    onDeleteClick = { taskId ->
                        onDeleteClick(taskId)
                    }
                )
            }
        }
    }
}