package com.android.readtracker.screens.home

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.readtracker.data.DataOrException
import com.android.readtracker.model.MBook
import com.android.readtracker.navigation.ReadTrackerScreens
import com.android.readtracker.repository.FireRepository
import com.android.readtracker.screens.update.showToast
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val repository: FireRepository
) : ViewModel() {
    val data: MutableState<DataOrException<List<MBook>, Boolean, Exception>> =
        mutableStateOf(DataOrException(listOf(), true, Exception("")))

    init {
        getAllBooksFromDatabase()
    }

    private fun getAllBooksFromDatabase() {
        viewModelScope.launch {
            data.value.loading = true
            data.value = repository.getAllBooksFromDatabase()
            if (!data.value.data.isNullOrEmpty()) data.value.loading = false
        }
        Log.d("GET", "getAllBooksFromDatabase: ${data.value.data?.toList().toString()}")

    }

    fun deleteBook(
        bookId: String,
        onSuccess: () -> Unit = {}
    ) {
        repository.deleteBook(bookId = bookId) {
            onSuccess()
        }
    }

    fun updateBook(
        bookToUpdate: Map<String, Comparable<*>?>,
        bookId: String,
        onSuccess: () -> Unit = {}

    ) {
        repository.updateBookFromDatabase(
            bookToUpdate, bookId
        ) {
            onSuccess()
        }

    }


}