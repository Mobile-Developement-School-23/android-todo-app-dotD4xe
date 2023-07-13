package com.example.todolist.presentation.todo.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todolist.presentation.todo.model.TodoAction
import com.example.todolist.presentation.todo.model.TodoState
import com.example.todolist.presentation.todo.theme.Blue
import com.example.todolist.presentation.todo.theme.BlueTranslucent
import com.example.todolist.presentation.todo.theme.ExtendedTheme
import com.example.todolist.presentation.util.toText
import java.util.Date

@Composable
fun TaskEditDateField(
    state: TodoState,
    onAction: (TodoAction) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 16.dp)
            .height(80.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val dateText = remember(state.deadline) { state.deadline.toText() }
        var openDialog by remember { mutableStateOf(false) }

        DatePicker(
            date = state.deadline,
            open = openDialog,
            closePicker = { openDialog = false },
            onAction = onAction
        )

        Column {
            Text(
                text = "Сделать до",
                modifier = Modifier.padding(start = 4.dp),
                color = ExtendedTheme.colors.labelPrimary
            )

            AnimatedVisibility(visible = state.isDeadlineVisible) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { openDialog = true }
                        .padding(4.dp)
                ) {
                    Text(dateText, color = Blue)
                }
            }
        }

        Switch(
            checked = state.isDeadlineVisible,
            onCheckedChange = { onAction(TodoAction.UpdateDeadlineVisibility(!state.isDeadlineVisible)) },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Blue,
                checkedTrackColor = BlueTranslucent,
                uncheckedThumbColor = ExtendedTheme.colors.backElevated,
                uncheckedTrackColor = ExtendedTheme.colors.supportOverlay,
                uncheckedBorderColor = ExtendedTheme.colors.supportOverlay,
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePicker(
    date: Date,
    open: Boolean,
    closePicker: () -> Unit,
    onAction: (TodoAction) -> Unit
) {
    if (open) {
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = date.time)
        val confirmEnabled by remember(datePickerState.selectedDateMillis) {
            derivedStateOf { datePickerState.selectedDateMillis != null }
        }

        DatePickerDialog(
            onDismissRequest = closePicker,
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let {
                            onAction(TodoAction.UpdateDeadline(it))
                        }
                        closePicker()
                    },
                    enabled = confirmEnabled
                ) {
                    Text("Ok")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = closePicker
                ) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}