package com.example.todolist.presentation.todo.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.example.todolist.R
import com.example.todolist.domain.entity.Importance
import com.example.todolist.presentation.todo.model.TodoAction
import com.example.todolist.presentation.todo.theme.ExtendedTheme
import com.example.todolist.presentation.todo.theme.GrayLight

@Composable
fun todoImportance(
    importance: Importance,
    onAction: (TodoAction) -> Unit
) {
    val isHighImportance = remember(importance) { importance == Importance.IMPORTANT }
    var menuExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(top = 24.dp, bottom = 12.dp)
            .padding(horizontal = 12.dp)
            .clip(RoundedCornerShape(6.dp))
            .clickable { menuExpanded = true }
            .padding(4.dp)
    ) {
        Text(
            text = "Priority",
            color = ExtendedTheme.colors.labelPrimary
        )
        Text(
            //iiii
            text = importance.toString(),
            modifier = Modifier.padding(top = 4.dp),
            color = if (isHighImportance) Red else ExtendedTheme.colors.labelTertiary
        )

        menu(
            expanded = menuExpanded,
            hideMenu = { menuExpanded = false },
            onAction = onAction
        )
    }
}

@Composable
private fun menu(
    expanded: Boolean,
    hideMenu: () -> Unit,
    onAction: (TodoAction) -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = hideMenu,
        modifier = Modifier
            .background(ExtendedTheme.colors.backElevated),
        offset = DpOffset(x = 52.dp, y = (-18).dp)
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
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.low_importance),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            },
            colors = MenuDefaults.itemColors(
                textColor = ExtendedTheme.colors.labelPrimary,
                leadingIconColor = GrayLight
            )
        )

        DropdownMenuItem(
            text = { Text(stringResource(R.string.hight)) },
            onClick = {
                onAction(TodoAction.UpdateImportance(Importance.IMPORTANT))
                hideMenu()
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.hight_importance),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            },
            colors = MenuDefaults.itemColors(
                textColor = Red,
                leadingIconColor = Red
            )
        )
    }
}