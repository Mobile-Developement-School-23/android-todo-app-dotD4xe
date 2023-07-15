package com.example.todolist.presentation.todo.components

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Surface

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todolist.R
import com.example.todolist.presentation.todo.model.TodoAction
import com.example.todolist.presentation.todo.theme.ExtendedTheme
import com.example.todolist.presentation.todo.theme.TodoAppTheme

@Composable
fun InputField(
    initialDescription: String,
    onAction: (TodoAction) -> Unit
) {
    val description = remember { mutableStateOf(initialDescription) }

    Surface(
        modifier = Modifier.padding(17.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        TextField(
            value = description.value,
            onValueChange = {
                description.value = it
                onAction(TodoAction.ContentChange(it))
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            textStyle = TextStyle(
                textAlign = TextAlign.Start,
                color = ExtendedTheme.colors.labelPrimary
            ),
            singleLine = false,
            maxLines = 500,
            minLines = 6,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = ExtendedTheme.colors.backSecondary,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = ExtendedTheme.colors.labelPrimary
            ),
            placeholder = {
                Text(
                    text = stringResource(R.string.text_for_field),
                    style = TextStyle(color = ExtendedTheme.colors.labelTertiary)
                )
            }
        )
    }
}

@Preview(showBackground = false)
@Composable
fun InputFieldPreview() {
    TodoAppTheme {
        InputField(onAction = {}, initialDescription = "testing")
    }
}
