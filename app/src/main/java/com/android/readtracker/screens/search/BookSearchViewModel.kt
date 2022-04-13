package com.android.readtracker.screens.search

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.readtracker.data.Resource
import com.android.readtracker.model.Item
import com.android.readtracker.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.ResponseCache
import javax.inject.Inject

@HiltViewModel
class BookSearchViewModel @Inject constructor(private val repository: BookRepository) :
    ViewModel() {

    var list: List<Item> by mutableStateOf(listOf())


    init {
        loadBooks()
    }

    private fun loadBooks() {
        searchBooks("android")
    }

    fun searchBooks(query: String) {
        viewModelScope.launch(Dispatchers.Default) {
            if (query.isEmpty())
                return@launch

            try {

                when (val response = repository.getBooksWithName(query)) {
                    is Resource.Success -> {
                        list = response.data!!
                    }
                    is Resource.Loading -> {

                    }
                    is Resource.Error -> {
                        Log.e("Network","searchBooks:${response.message}")
                    }
                }

            } catch (exception: Exception) {

            }
        }
    }
}