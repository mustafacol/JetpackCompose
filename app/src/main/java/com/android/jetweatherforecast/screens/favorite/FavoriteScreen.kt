package com.android.jetweatherforecast.screens.favorite

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.android.jetweatherforecast.WeatherApp
import com.android.jetweatherforecast.model.Favorite
import com.android.jetweatherforecast.navigation.WeatherScreens
import com.android.jetweatherforecast.widgets.WeatherAppBar
import kotlinx.coroutines.flow.collectLatest

@Composable
fun FavoriteScreen(
    navController: NavController,
    favoriteViewModel: FavoriteViewModel = hiltViewModel()
) {

    Scaffold(
        topBar = {
            WeatherAppBar(
                title = "Favorite Cities",
                navController = navController,
                icon = Icons.Default.ArrowBack,
                isMainScreen = false,
            ) {
                navController.popBackStack()
            }
        }
    ) {
        Surface(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth()
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                val list = favoriteViewModel.favList.collectAsState()

                LazyColumn {
                    items(list.value) {
                        CityRowItem(it, navController, favoriteViewModel)
                    }
                }
            }
        }

    }

}

@Composable
fun CityRowItem(
    favorite: Favorite,
    navController: NavController,
    favoriteViewModel: FavoriteViewModel
) {
    Card(
        modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth()
            .height(50.dp)
            .clickable {
                navController.navigate(WeatherScreens.MainScreen.name + "/${favorite.city}")
            },
        shape = RoundedCornerShape(25),
        backgroundColor = Color(0xFFB2DFDB),
        elevation = 6.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = favorite.city,
                style = MaterialTheme.typography.caption,
                fontSize = 17.sp
            )
            Surface(
                shape = CircleShape,
                color = Color.LightGray
            ) {
                Text(
                    modifier = Modifier.padding(6.dp),

                    text = favorite.country,
                    style = MaterialTheme.typography.caption
                )
            }

            Icon(
                modifier = Modifier.clickable { favoriteViewModel.deleteFavorite(favorite = favorite) },
                imageVector = Icons.Rounded.Delete,
                contentDescription = "Delete Button",
                tint = Color.Red

            )
        }
    }

}
