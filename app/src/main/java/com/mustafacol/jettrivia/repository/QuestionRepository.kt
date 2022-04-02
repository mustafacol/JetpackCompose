package com.mustafacol.jettrivia.repository

import android.util.Log
import com.mustafacol.jettrivia.data.DataOrException
import com.mustafacol.jettrivia.model.QuestionItem
import com.mustafacol.jettrivia.network.QuestionApi
import javax.inject.Inject

class QuestionRepository @Inject constructor(
    private val api: QuestionApi,
) {
    private val dataOrException = DataOrException<ArrayList<QuestionItem>, Boolean, Exception>()

    suspend fun getAllQuestions(): DataOrException<ArrayList<QuestionItem>, Boolean, Exception> {
        try {
            dataOrException.loading = true
            dataOrException.data = api.getAllQuestions()
            if (dataOrException.data.toString().isNullOrEmpty()) dataOrException.loading = false
        } catch (exception: Exception) {
            dataOrException.e = exception
            Log.d("Exception", "getAllQuestions:${dataOrException.e!!.localizedMessage}")
        }

        return dataOrException
    }
}