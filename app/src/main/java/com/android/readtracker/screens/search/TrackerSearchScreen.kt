package com.android.readtracker.screens.search

import androidx.compose.foundation.Image
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
        isSingleLine = true,
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
    viewModel: BookSearchViewModel
) {
    val listOfBooks = listOf(
        MBook(id = "1", title = "Hello World", authors = "You", notes = null),
        MBook(id = "2", title = "Hello World", authors = "You", notes = null),
        MBook(id = "3", title = "Hello World", authors = "You", notes = null),
        MBook(id = "4", title = "Hello World", authors = "You", notes = null),
        MBook(id = "5", title = "Hello World", authors = "You", notes = null),

        )
    LazyColumn {
        items(listOfBooks) { bookItem ->
            BookRow(bookItem)
        }
    }
}

@Preview
@Composable
fun BookRow(book: MBook = MBook("1", "Android For Dummies", "Jane Doe", null)) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(start = 6.dp, end = 6.dp, bottom = 6.dp),
        elevation = 6.dp
    ) {
        Row(
            modifier = Modifier.padding(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberImagePainter("http://books.google.com/books/content?id=Y5MqEAAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api"),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(80.dp)
                    .padding(4.dp)
            )

            Column(

            ) {
                Text(
                    text = book.title.toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Author: ${book.authors}",
                    overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.caption
                )
                Text(
                    text = "Date: 2020-09-09",
                    overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.caption

                )
                Text(
                    text = "[Computers]",
                    overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.caption

                )


            }

        }
    }
}