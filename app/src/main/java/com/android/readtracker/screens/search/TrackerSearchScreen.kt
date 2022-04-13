package com.android.readtracker.screens.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.android.readtracker.components.InputField
import com.android.readtracker.components.ReadTrackerTopBar
import com.android.readtracker.model.Item
import com.android.readtracker.model.MBook
import com.android.readtracker.navigation.ReadTrackerScreens

@Composable
fun TrackerSearchScreen(
    navController: NavController,
    viewModel: BookSearchViewModel = hiltViewModel()
) {

    Scaffold(
        topBar = {
            ReadTrackerTopBar(
                title = "Search Screen",
                icon = Icons.Default.ArrowBack,
                navController = navController,
                showProfile = false
            ) {
                navController.navigate(ReadTrackerScreens.ReadTrackerHomeScreen.name)
            }
        }
    ) {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier.padding(8.dp)
            ) {

                SearchForm(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    viewModel.searchBooks(it)
                }
                Spacer(
                    Modifier
                        .height(15.dp)
                )

                BookList(navController = navController, viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchForm(
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    viewModel: BookSearchViewModel = hiltViewModel(),
    hint: String = "Search",
    onSearch: (String) -> Unit = {}
) {
    val searchQueryState = rememberSaveable { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val valid = remember(searchQueryState.value) {
        searchQueryState.value.trim().isNotEmpty()
    }
    InputField(
        valueState = searchQueryState,
        label = hint,
        enabled = true,
        //activeSearch = true,
        isSingleLine = true,
//        valueChange = {
//            viewModel.searchBooks(it)
//        },
        onAction = KeyboardActions {
            if (!valid) return@KeyboardActions
            onSearch(searchQueryState.value.trim())
            searchQueryState.value = ""
            keyboardController?.hide()
        }

    )
}

@Composable
fun BookList(
    navController: NavController,
    viewModel: BookSearchViewModel = hiltViewModel()
) {
    val listOfBooks = viewModel.list
    if (viewModel.isLoading) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LinearProgressIndicator()
        }
    } else {
        LazyColumn {
            items(listOfBooks) { bookItem ->
                BookRow(bookItem, navController)
            }
        }
    }

}

@Composable
fun BookRow(book: Item, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(start = 6.dp, end = 6.dp, bottom = 6.dp)
            .clickable {
                navController.navigate(ReadTrackerScreens.DetailScreen.name + "/${book.id}")
            },
        elevation = 6.dp
    ) {
        Row(
            modifier = Modifier.padding(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val imageUrl = book.volumeInfo.imageLinks?.smallThumbnail
                ?: "https://upload.wikimedia.org/wikipedia/commons/thumb/5/50/Closed_Book_Icon.svg/512px-Closed_Book_Icon.svg.png"
            Image(
                painter = rememberImagePainter(imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(80.dp)
                    .padding(4.dp)
            )

            Column(

            ) {
                Text(
                    text = book.volumeInfo.title.toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Author: ${book.volumeInfo.authors ?: book.volumeInfo.publisher}",
                    overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.caption
                )
                Text(
                    text = "Date: ${book.volumeInfo.publishedDate ?: "Unknown"}",
                    overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.caption

                )
                Text(
                    text = "${book.volumeInfo.categories ?: "[]"}",
                    overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.caption

                )


            }

        }
    }
}