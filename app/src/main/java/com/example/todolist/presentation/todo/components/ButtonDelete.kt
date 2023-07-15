package com.example.todolist.presentation.todo.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.todolist.R
import com.example.todolist.presentation.todo.model.TodoAction
import com.example.todolist.presentation.todo.theme.ExtendedTheme
import com.example.todolist.presentation.todo.theme.Red
import com.example.todolist.presentation.todo.theme.TodoAppTheme

@Composable
fun ButtonDelete(
    enabled: Boolean,
    onClick: (TodoAction) -> Unit,
    navController: NavController,
) {
    Row(
        modifier = Modifier
            .clickable(enabled = enabled, onClick = {
                onClick(TodoAction.DeleteTodo)
                navController.navigateUp()
            } )
            .padding(start = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        val tint =
            if (enabled) Red
            else ExtendedTheme.colors.labelDisable

        Icon(
            imageVector = Icons.Filled.Delete,
            contentDescription = null,
            tint = tint,
        )
        Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
        Text(
            text = stringResource(R.string.delete_label),
            color = tint,
            style = MaterialTheme.typography.body1,
        )
    }
}

@Preview(showBackground = false)
@Composable
fun ButtonDeletePreview() {
    val localNavController = compositionLocalOf<NavController> { error("No NavController found!") }
    val navController = rememberNavController()
    CompositionLocalProvider(localNavController provides navController) {
        TodoAppTheme {
            ButtonDelete(enabled = true, onClick = {}, navController)
        }
    }
}
