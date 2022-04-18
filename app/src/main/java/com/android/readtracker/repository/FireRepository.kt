package com.android.readtracker.repository

import android.util.Log
import com.android.readtracker.data.DataOrException
import com.android.readtracker.model.MBook
import com.android.readtracker.navigation.ReadTrackerScreens
import com.android.readtracker.screens.update.showToast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.Exception
import kotlin.coroutines.coroutineContext

class FireRepository @Inject constructor(
    private val queryBook: Query
) {

    suspend fun getAllBooksFromDatabase(): DataOrException<List<MBook>, Boolean, Exception> {
        val dataOrException = DataOrException<List<MBook>, Boolean, Exception>()

        try {
            dataOrException.loading = true
            dataOrException.data = queryBook.get().await().documents.map { documentSnapshot ->
                documentSnapshot.toObject(MBook::class.java)!!
            }
            if (!dataOrException.data.isNullOrEmpty()) dataOrException.loading = false
        } catch (exception: FirebaseFirestoreException) {
            dataOrException.e = exception
        }
        return dataOrException
    }

    fun deleteBook(
        bookId: String,
        onSuccess: () -> Unit = {}
    ) {
        FirebaseFirestore
            .getInstance()
            .collection("books")
            .document(bookId)
            .delete()
            .addOnSuccessListener {
                //Log.d("Delete", "Success")
                onSuccess.invoke()
            }
            .addOnFailureListener {
                Log.d("Delete", "Failure:${it.message}")
            }

    }

    fun updateBookFromDatabase(
        bookToUpdate: Map<String, Comparable<*>?>,
        bookId: String,
        onSuccess: () -> Unit = {}

    ) {

        FirebaseFirestore
            .getInstance()
            .collection("books")
            .document(bookId)
            .update(bookToUpdate)
            .addOnCompleteListener {
                //Log.d("UpdateSuccess", "UpdateResult: ${it.isComplete}")

                onSuccess.invoke()

            }
            .addOnFailureListener {
                Log.w("UpdateFail", "Error updating document $it")
            }
    }
}
