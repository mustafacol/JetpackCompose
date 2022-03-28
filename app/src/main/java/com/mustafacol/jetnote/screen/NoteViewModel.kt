package com.mustafacol.jetnote.screen

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mustafacol.jetnote.data.NotesDataSource
import com.mustafacol.jetnote.model.Note
import com.mustafacol.jetnote.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(private val noteRepository: NoteRepository) : ViewModel() {
    //private var noteList = mutableStateListOf<Note>()
    private val _noteList = MutableStateFlow<List<Note>>(emptyList())
    val noteList = _noteList.asStateFlow()

    init {
        viewModelScope.launch {
            noteRepository.getAllNotes()
                .collect { listOfNotes ->
                    if (!listOfNotes.isNullOrEmpty())
                        _noteList.value = listOfNotes
                }
        }

    }

    fun addNote(note: Note) = viewModelScope.launch { noteRepository.addNote(note) }
    fun updateNote(note: Note) = viewModelScope.launch { noteRepository.updateNote(note) }
    fun deleteNote(note: Note) = viewModelScope.launch { noteRepository.deleteNote(note) }
}