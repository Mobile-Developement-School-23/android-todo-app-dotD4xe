package com.example.todolist.presentation.todo.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.todolist.presentation.todo.model.TodoAction
import com.example.todolist.presentation.todo.theme.ExtendedTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    navController: NavController,
    onAction: (TodoAction) -> Unit
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = ExtendedTheme.colors.backPrimary,
            navigationIconContentColor = ExtendedTheme.colors.labelPrimary
        ),
        modifier = Modifier
            .statusBarsPadding(),
        navigationIcon = {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                modifier = Modifier.clickable { navController.navigateUp() }
            )
        },
        title = {},
        actions = {
            TextButton(
                onClick = {
                    onAction(TodoAction.SaveTodo)
                    navController.popBackStack()
                },
//                            enabled = content.isNotBlank(),
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Color.Blue,
//                                disabledContentColor = saveButtonColor
                )
            ) {
                Text(
                    text = "save",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    )
}