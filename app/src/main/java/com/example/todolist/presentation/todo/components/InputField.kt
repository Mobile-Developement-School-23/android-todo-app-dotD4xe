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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.todolist.presentation.todo.model.TodoAction
import com.example.todolist.presentation.todo.theme.ExtendedTheme

@Composable
fun inputField(
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
            minLines = 4,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = ExtendedTheme.colors.backSecondary,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = ExtendedTheme.colors.labelPrimary
            ),
            placeholder = {
                Text(
                    text = "Введите описание",
                    style = TextStyle(color = ExtendedTheme.colors.labelPrimary)
                )
            }
        )
    }


//    BasicTextField(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(top = 8.dp)
//            .padding(horizontal = 16.dp),
//        value = description,
//        onValueChange = { onAction(TodoAction.ContentChange(it)) },
//        textStyle = MaterialTheme.typography.bodyLarge.copy(
//            color = ExtendedTheme.colors.labelPrimary
//        ),
//        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
//        minLines = 3,
//        cursorBrush = SolidColor(ExtendedTheme.colors.labelPrimary)
//    ) { innerTextField ->
//
//        Card(
//            colors = CardDefaults.cardColors(
//                containerColor = ExtendedTheme.colors.backSecondary,
//                contentColor = ExtendedTheme.colors.labelTertiary
//            )
//        ) {
//
//            Box(
//                modifier = Modifier
//                    .padding(16.dp)
//            ) {
//                if (description.isEmpty())
//                    Text("test")
//                innerTextField.invoke()
//            }
//        }
//    }
}