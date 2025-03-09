package com.example.sampletodoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.sampletodoapp.models.TodoItem
import com.example.sampletodoapp.ui.theme.SampleTodoAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SampleTodoAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TodoApp()
                }
            }
        }
    }
}

@Composable
fun TodoApp() {
    var appTodos by remember {
        mutableStateOf(listOf<TodoItem>())
    }
    var textInput by remember {
        mutableStateOf("")
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
        ) {
            Text(
                text = "This is my sample Todo App",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .padding(bottom = 16.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = textInput,
                    onValueChange = { textInput = it },
                    label = { Text("Add a task") },
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {
                        if (textInput.trim().isNotEmpty()) {
                            appTodos = appTodos + TodoItem(
                                id = System.currentTimeMillis(),
                                text = textInput,
                                isCompleted = false
                            )
                            textInput = ""
                        }
                    }
                ) {
                    Text("Add Task")
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            LazyColumn {
                items(appTodos, key = { it.id }) { todo ->
                    TodoItemRow(
                        todo = todo,
                        onToggle = {
                            appTodos = appTodos.map {
                                if (it.id == todo.id) it.copy(isCompleted = !it.isCompleted)
                                else it
                            }
                        },
                        onDelete = {
                            appTodos = appTodos.filter { it.id != todo.id }
                        }
                    )
                }
            }

        }
    }
}

@Composable
fun TodoItemRow(
    todo: TodoItem,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = todo.isCompleted,
            onCheckedChange = { onToggle() }
        )

        Text(
            text = todo.text,
            style = if (todo.isCompleted) {
                TextStyle(textDecoration = TextDecoration.LineThrough)
            } else {
                LocalTextStyle.current
            },
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
        )

        IconButton(onDelete) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete Task"
            )
        }

    }

}