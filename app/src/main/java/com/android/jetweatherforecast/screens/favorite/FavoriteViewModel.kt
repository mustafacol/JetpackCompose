package com.android.jetweatherforecast.screens.favorite

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.jetweatherforecast.model.Favorite
import com.android.jetweatherforecast.repository.WeatherDbRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.internal.wait
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(private val repository: WeatherDbRepository) :
    ViewModel() {
    private val _favList = MutableStateFlow<List<Favorite>>(emptyList())
    val favList = _favList.asStateFlow()
    var isFav = MutableLiveData(true)

    init {
        getFavorites()
    }

    fun getFavorites() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getFavorites().distinctUntilChanged().collect { listOfFavs ->
                if (listOfFavs.isNullOrEmpty())
                    Log.d("TAG", "Empty Favs")
                else {
                    _favList.value = listOfFavs

                    Log.d("TAG", ":${favList.value}")
                }
            }
        }
    }


    fun insertFavorite(favorite: Favorite) =
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.insertFavorite(favorite)
            }
            repository.getFavorites()
        }

    fun updateFavorite(favorite: Favorite) =
        viewModelScope.launch { repository.updateFavorite(favorite) }

    fun deleteFavorite(favorite: Favorite) =
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.deleteFavorite(favorite)

            }
            repository.getFavorites()
        }


}