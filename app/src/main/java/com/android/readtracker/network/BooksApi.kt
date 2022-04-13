package com.android.readtracker.network

import com.android.readtracker.model.Book
import com.android.readtracker.model.Item
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface BooksApi {

    @GET("volumes")
    suspend fun getBooksWithName(@Query("q") query: String): Book

    @GET("volumes/{bookId}")
    suspend fun getBookWithId(@Path("bookId") bookId:String): Item
}