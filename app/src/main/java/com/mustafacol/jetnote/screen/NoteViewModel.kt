package com.mustafacol.jetnote.screen

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import com.mustafacol.jetnote.data.NotesDataSource
import com.mustafacol.jetnote.model.Note

class NoteViewModel : ViewModel() {
    private var noteList = mutableStateListOf<Note>()

    init {
        noteList.addAll(NotesDataSource().loadData())
    }

    fun addNote(note: Note) {
        noteList.add(note)
    }

    fun removeNote(note: Note) {
        noteList.remove(note)
    }

    fun getNoteList(): List<Note> {
        return noteList
    }
}