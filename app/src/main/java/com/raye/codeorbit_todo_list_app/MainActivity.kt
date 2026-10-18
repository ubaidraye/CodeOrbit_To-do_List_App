package com.raye.codeorbit_todo_list_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.raye.codeorbit_todo_list_app.presentation.tasklist.TaskListScreen
import com.raye.codeorbit_todo_list_app.ui.theme.CodeOrbitToDoListAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CodeOrbitToDoListAppTheme {
                TaskListScreen()
            }
        }
    }
}
