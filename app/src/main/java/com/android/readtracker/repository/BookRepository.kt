package com.android.readtracker.repository

import com.android.readtracker.data.DataOrException
import com.android.readtracker.data.Resource
import com.android.readtracker.model.Item
import com.android.readtracker.network.BooksApi
import javax.inject.Inject
import kotlin.Exception

class BookRepository @Inject constructor(private val api: BooksApi) {
    private val dataOrException = DataOrException<List<Item>, Boolean, Exception>()
    private val bookInfoDataOrException = DataOrException<Item, Boolean, Exception>()


    suspend fun getBooksWithName(searchQuery: String): Resource<List<Item>> {
        return try {
            Resource.Loading(data = "Loading")
            val itemList = api.getBooksWithName(searchQuery).items
            if (itemList.isNotEmpty()) Resource.Loading(false)
            Resource.Success(data = itemList)
        } catch (exception: Exception) {
            Resource.Error(message = exception.message.toString())
        }

    }

    suspend fun getBookInfoWithId(bookId: String): Resource<Item> {
        val response = try {
            Resource.Loading(data = "Loading")
            api.getBookWithId(bookId)

        } catch (exception: Exception) {
            return Resource.Error(message = exception.message.toString())
        }
        Resource.Loading(false)
        return Resource.Success(data = response)
    }

//
//    suspend fun getBooks(searchQuery: String): DataOrException<List<Item>, Boolean, Exception> {
//        try {
//            dataOrException.loading = true
//            dataOrException.data = api.getBooksWithName(searchQuery).items
//
//            if (dataOrException.data!!.isNotEmpty())
//                dataOrException.loading = false
//        } catch (e: Exception) {
//            dataOrException.e = e
//        }
//
//        return dataOrException
//    }
//
//    suspend fun getBookInfoWithId(bookId: String): DataOrException<Item, Boolean, Exception> {
//        try {
//            bookInfoDataOrException.loading = true
//            bookInfoDataOrException.data = api.getBookWithId(bookId = bookId)
//
//            if (bookInfoDataOrException.data.toString().isNotEmpty()){
//                bookInfoDataOrException.loading = false
//            }
//        } catch (e: Exception) {
//            bookInfoDataOrException.e=e
//        }
//
//        return bookInfoDataOrException
//    }
}