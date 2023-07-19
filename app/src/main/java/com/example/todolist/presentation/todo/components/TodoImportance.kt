package com.example.todolist.presentation.todo.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todolist.R
import com.example.todolist.domain.entity.Importance
import com.example.todolist.presentation.todo.model.TodoAction
import com.example.todolist.presentation.todo.theme.ExtendedTheme
import com.example.todolist.presentation.todo.theme.TodoAppTheme

@Composable
fun TodoImportance(
    importance: Importance,
    onAction: (TodoAction) -> Unit
) {
    val isHighImportance = remember(importance) { importance == Importance.IMPORTANT }
    var menuExpanded by remember { mutableStateOf(false) }

    val textImportance = when(importance) {
        Importance.BASIC -> stringResource(R.string.no)
        Importance.IMPORTANT -> stringResource(R.string.hight)
        else -> stringResource(R.string.low)
    }

    Column(
        modifier = Modifier
            .padding(top = 24.dp, bottom = 12.dp)
            .padding(horizontal = 12.dp)
            .clip(RoundedCornerShape(6.dp))
            .clickable { menuExpanded = true }
            .padding(4.dp)
    ) {
        Text(
            text = stringResource(R.string.importance),
            color = ExtendedTheme.colors.labelPrimary
        )
        Text(
            text = textImportance,
            modifier = Modifier.padding(top = 4.dp),
            color = if (isHighImportance) Red else ExtendedTheme.colors.labelTertiary
        )

        Menu(
            expanded = menuExpanded,
            hideMenu = { menuExpanded = false },
            onAction = onAction
        )
    }
}

@Composable
private fun Menu(
    expanded: Boolean,
    hideMenu: () -> Unit,
    onAction: (TodoAction) -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = hideMenu,
        modifier = Modifier
            .background(ExtendedTheme.colors.backElevated)
    ) {
        DropdownMenuItem(
            text = { Text(stringResource(R.string.no)) },
            onClick = {
                onAction(TodoAction.UpdateImportance(Importance.BASIC))
                hideMenu()
            },
            colors = MenuDefaults.itemColors(
                textColor = ExtendedTheme.colors.labelPrimary,
            )
        )

        DropdownMenuItem(
            text = { Text(stringResource(R.string.low)) },
            onClick = {
                onAction(TodoAction.UpdateImportance(Importance.LOW))
                hideMenu()
            },
            colors = MenuDefaults.itemColors(
                textColor = ExtendedTheme.colors.labelPrimary,
            )
        )

        DropdownMenuItem(
            text = { Text(stringResource(R.string.hight)) },
            onClick = {
                onAction(TodoAction.UpdateImportance(Importance.IMPORTANT))
                hideMenu()
            },
            colors = MenuDefaults.itemColors(
                textColor = Red,
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TodoImportancePreview() {
    TodoAppTheme {
        TodoImportance(Importance.IMPORTANT, onAction = {})
    }
}

