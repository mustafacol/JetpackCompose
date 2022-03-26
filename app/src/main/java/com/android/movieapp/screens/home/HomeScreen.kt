package com.android.movieapp.screens.home

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.android.movieapp.model.getMovies
import com.android.movieapp.navigation.MovieScreens
import com.android.movieapp.widgets.MovieRow

@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(backgroundColor = Color.LightGray, elevation = 5.dp) {
                Text(text = "Movies")
            }
        }
    ) {
        MainContent(navController)
    }
}


@Composable
fun MainContent(navController: NavController) {
    val moviesList = getMovies()
    Surface(color = MaterialTheme.colors.background) {
        LazyColumn {
            items(moviesList) {
                MovieRow(movie = it) { movie ->
                    navController.navigate(route = MovieScreens.DetailsScreen.name + "/$movie")
                }
            }
        }

    }
}


