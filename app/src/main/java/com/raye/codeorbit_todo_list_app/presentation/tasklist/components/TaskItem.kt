package com.raye.codeorbit_todo_list_app.presentation.tasklist.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.raye.codeorbit_todo_list_app.domain.model.TodoTask

@Composable
fun TaskItem(
    task: TodoTask,
    onTaskClick: () -> Unit = {},
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp, horizontal = 8.dp)
            .clickable(onClick = onTaskClick)
    ) {
        Column {
            Text(
                text = task.title,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = task.description,
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}