package com.mustafacol.jetnote.components

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.TextFieldDefaults.textFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import com.mustafacol.jetnote.model.Note

@ExperimentalComposeUiApi
@Composable
fun NoteInputText(
    modifier: Modifier = Modifier,
    text: String,
    label: String,
    maxLine: Int = 1,
    onTextChange: (String) -> Unit,
    onImeAction: () -> Unit = {}
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    TextField(
        value = text,
        onValueChange = onTextChange,
        colors = textFieldColors(
            backgroundColor = Color.Transparent
        ),
        maxLines = maxLine,
        label = { Text(text = label) },
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = {
            onImeAction()
            keyboardController?.hide()
        }),
        modifier = modifier
    )
}

@Composable
fun NoteButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        shape = CircleShape,
        enabled = enabled,
        modifier = modifier,
    ) {
        Text(text = text)
    }
}

@Composable
fun AlertDialogBuild(
    title: String = "Delete",
    content: String = "",
    confirmButtonText: String = "Delete",
    dismissButtonText: String = "Cancel",
    note: Note = Note(title = "Hello", description = "Content"),
    confirmButtonClick: (Note) -> Unit = {},
    dismissButtonClick: () -> Unit = {},

) {
    AlertDialog(
        onDismissRequest = {

        },
        title = { Text(text = title) },
        text = { Text(text = content) },
        confirmButton = {
            Button(onClick = { confirmButtonClick(note) }) {
                Text(text = confirmButtonText)
            }
        },
        dismissButton = {
            Button(
                onClick = { dismissButtonClick() }
            ) {
                Text(text = dismissButtonText)
            }
        }
    )
}