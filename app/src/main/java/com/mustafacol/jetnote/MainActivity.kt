package com.mustafacol.jetnote

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mustafacol.jetnote.data.NotesDataSource
import com.mustafacol.jetnote.model.Note
import com.mustafacol.jetnote.screen.NoteScreen
import com.mustafacol.jetnote.screen.NoteViewModel
import com.mustafacol.jetnote.ui.theme.JetNoteTheme

class MainActivity : ComponentActivity() {
    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetNoteTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    val noteViewModel: NoteViewModel by viewModels()
                    NoteApp(noteViewModel = noteViewModel)
                }
            }
        }
    }
}

@ExperimentalComposeUiApi
@Composable
fun NoteApp(noteViewModel: NoteViewModel = viewModel()) {
    val notes = noteViewModel.getNoteList()
    NoteScreen(
        notes = notes,
        onAddNote = {
            noteViewModel.addNote(it)
        },
        onRemoveNote = {
            noteViewModel.removeNote(it)
        })
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JetNoteTheme {
    }
}