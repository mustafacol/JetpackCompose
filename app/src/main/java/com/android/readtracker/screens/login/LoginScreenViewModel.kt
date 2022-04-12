package com.android.readtracker.screens.login

import android.util.Log
import androidx.compose.ui.text.capitalize
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.readtracker.model.MUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class LoginScreenViewModel : ViewModel() {
    //val loadingState = MutableStateFlow(LoadingState.IDLE)
    private val auth: FirebaseAuth = Firebase.auth

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        firstName: String?,
        lastName: String?,
        onSuccess: () -> Unit
    ) {
        try {
            if (_loading.value == false) {
                _loading.value = true

                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            createUser(firstName = firstName, lastName = lastName)
                            onSuccess()
                        } else {
                            Log.d(
                                "FB",
                                "createUserWithEmailAndPassword: ${task.exception?.message}"
                            )
                        }
                        _loading.value = false

                    }
            }

        } catch (ex: Exception) {
            Log.d("FB", "createUserWithEmailAndPassword: $ex.message")
        }
    }

    fun signInWithEmailAndPassword(email: String, password: String, onSuccess: () -> Unit) =
        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("FB", task.result.toString())
                        onSuccess()
                    } else {

                    }
                }
            } catch (ex: Exception) {
                Log.d("FB", "signInWithEmailAndPassword: $ex.message")
            }
        }


    private fun createUser(firstName: String?, lastName: String?) {
        val userId = auth.currentUser?.uid
        val mUser= MUser(
            userId = userId.toString(),
            firstName = firstName.toString(),
            lastName = lastName.toString(),
            avatarUrl = "",
            quote = "Life is great",
            profession = "Android Developer",
            id = null,
        ).toMap()

        FirebaseFirestore.getInstance().collection("users").add(mUser)
    }

}