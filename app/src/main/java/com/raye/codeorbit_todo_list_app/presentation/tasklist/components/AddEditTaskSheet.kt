package com.raye.codeorbit_todo_list_app.presentation.tasklist.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.raye.codeorbit_todo_list_app.domain.model.Priority
import com.raye.codeorbit_todo_list_app.domain.model.TodoTask


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTaskSheet(
    onDismissRequest: () -> Unit,
    isSheetOpen: Boolean,
    onSave: (
        title: String,
        description: String,
        priority: Priority,
        isCompleted: Boolean
    ) -> Unit,
    taskToEdit: TodoTask? = null
) {
    val sheetState = rememberModalBottomSheetState()

    var taskTitle by rememberSaveable { mutableStateOf("") }
    var taskDescription by rememberSaveable { mutableStateOf("") }
    var selectedPriority by rememberSaveable {
        mutableStateOf(Priority.MEDIUM)
    }
    var isCompleted by rememberSaveable { mutableStateOf(false) }
    var priorityMenuExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(isSheetOpen, taskToEdit?.id) {
        if (isSheetOpen) {
            taskTitle = taskToEdit?.title.orEmpty()
            taskDescription = taskToEdit?.description.orEmpty()
            selectedPriority = taskToEdit?.priority ?: Priority.MEDIUM
            isCompleted = taskToEdit?.isCompleted ?: false
        }
    }

    if (isSheetOpen) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = onDismissRequest
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = if (taskToEdit == null) "Add Task" else "Edit Task",
                    style = MaterialTheme.typography.headlineSmall
                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = taskTitle,
                    onValueChange = { taskTitle = it },
                    label = { Text("Title") },
                    placeholder = { Text("e.g. Buy groceries") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    )
                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = taskDescription,
                    onValueChange = { taskDescription = it },
                    label = { Text("Description (optional)") },
                    placeholder = { Text("Add more details") },
                    minLines = 2,
                    maxLines = 4,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    )
                )

                // Priority dropdown
                ExposedDropdownMenuBox(
                    expanded = priorityMenuExpanded,
                    onExpandedChange = {
                        priorityMenuExpanded = !priorityMenuExpanded
                    }
                ) {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        value = selectedPriority.name
                            .lowercase()
                            .replaceFirstChar { it.uppercase() },
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Priority") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = priorityMenuExpanded
                            )
                        }
                    )

                    ExposedDropdownMenu(
                        expanded = priorityMenuExpanded,
                        onDismissRequest = {
                            priorityMenuExpanded = false
                        }
                    ) {
                        Priority.entries.forEach { priority ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        priority.name.lowercase()
                                            .replaceFirstChar { it.uppercase() }
                                    )
                                },
                                onClick = {
                                    selectedPriority = priority
                                    priorityMenuExpanded = false
                                }
                            )
                        }
                    }
                }

                // Completion switch
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Completed",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = if (isCompleted) {
                                "This task is complete"
                            } else {
                                "This task is still pending"
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Switch(
                        checked = isCompleted,
                        onCheckedChange = { isCompleted = it }
                    )
                }

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    enabled = taskTitle.isNotBlank(),
                    onClick = {
                        onSave(
                            taskTitle.trim(),
                            taskDescription.trim(),
                            selectedPriority,
                            isCompleted
                        )
                    }
                ) {
                    Text(
                        text = if (taskToEdit == null) {
                            "Create Task"
                        } else {
                            "Save Changes"
                        }
                    )
                }
            }
        }
    }
}
