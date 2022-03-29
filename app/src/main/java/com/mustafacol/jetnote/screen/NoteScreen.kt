package com.mustafacol.jetnote.screen

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mustafacol.jetnote.R
import com.mustafacol.jetnote.components.AlertDialogBuild
import com.mustafacol.jetnote.components.NoteButton
import com.mustafacol.jetnote.components.NoteInputText
import com.mustafacol.jetnote.data.NotesDataSource
import com.mustafacol.jetnote.model.Note
import com.mustafacol.jetnote.utils.formatDate
import java.time.format.DateTimeFormatter


@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@Composable
fun NoteScreen(
    notes: List<Note>,
    onAddNote: (Note) -> Unit,
    onRemoveNote: (Note) -> Unit,
    onUpdateNote: (Note) -> Unit
) {
    var title by remember {
        mutableStateOf("")
    }
    var description by remember {
        mutableStateOf("")
    }


    val context = LocalContext.current
    Column(modifier = Modifier.padding(6.dp)) {
        TopAppBar(
            title = { Text(text = stringResource(id = R.string.app_name)) },
            actions = {
                Icon(imageVector = Icons.Rounded.Notifications, contentDescription = "Icon")
            },
            backgroundColor = Color(0xFFDADFE3)
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            NoteInputText(
                modifier = Modifier.padding(8.dp),
                text = title,
                label = "Title",
                onTextChange = {
                    if (it.all { char -> char.isLetter() || char.isWhitespace() }) title = it
                })
            NoteInputText(
                modifier = Modifier.padding(8.dp),
                text = description,
                label = "Add a Note",
                onTextChange = {
                    if (it.all { char -> char.isLetter() || char.isWhitespace() }) description = it
                })
            NoteButton(text = "Add Note", onClick = {
                if (title.isNotEmpty() && description.isNotEmpty())
                    onAddNote(Note(title = title, description = description))
                title = ""
                description = ""

                Toast.makeText(context, "Note is added", Toast.LENGTH_SHORT).show()
            })

            Divider(modifier = Modifier.padding(10.dp))

            LazyColumn {
                items(notes) { item ->
                    NoteRow(
                        item = item,
                        onNoteClicked = {
                            title = it.title
                            description = it.description
                        },
                        onRemoveNote = {
                            onRemoveNote(it)
                        }

                    )
                }
            }
        }
    }
}

@ExperimentalFoundationApi
@Composable
private fun NoteRow(item: Note, onNoteClicked: (Note) -> Unit, onRemoveNote: (Note) -> Unit) {
    var showDialog by remember {
        mutableStateOf(false)
    }
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        shape = RoundedCornerShape(topEndPercent = 33, bottomStartPercent = 33),
        color = Color(0xFFDADFE3)
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 6.dp, horizontal = 14.dp)
                .combinedClickable(
                    onClick = { onNoteClicked(item) },
                    onLongClick = {
                        showDialog = true;
                    }

                ),

            horizontalAlignment = Alignment.Start
        ) {

            if(showDialog)
                AlertDialogBuild(
                    title = "Delete",
                    content = "Do you want to delete this note?",
                    confirmButtonClick = { onRemoveNote(item) },
                    confirmButtonText = "Okay",
                    dismissButtonClick = { showDialog = false }
                )
            Text(
                text = item.title,
                style = MaterialTheme.typography.subtitle2
            )
            Text(
                text = item.description,
                style = MaterialTheme.typography.subtitle1
            )
            Text(
                text = formatDate(item.entryDate.time),
                style = MaterialTheme.typography.caption
            )
        }
    }
}


@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@Preview
@Composable
fun NotesScreenPreview() {
    NoteScreen(notes = NotesDataSource().loadData(), {}, {}, {})
}