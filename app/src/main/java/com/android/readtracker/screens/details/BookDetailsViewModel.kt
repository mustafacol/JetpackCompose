package com.android.readtracker.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.readtracker.data.Resource
import com.android.readtracker.model.Item
import com.android.readtracker.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookDetailsViewModel @Inject constructor(private val repository: BookRepository) :
    ViewModel() {


    suspend fun getBookInfo(bookId: String): Resource<Item> {
        return repository.getBookInfoWithId(bookId = bookId)
    }
}