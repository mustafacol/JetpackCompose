package com.android.readtracker.screens.stats

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.sharp.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.substring
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.android.readtracker.components.ReadTrackerTopBar
import com.android.readtracker.model.MBook
import com.android.readtracker.navigation.ReadTrackerScreens
import com.android.readtracker.screens.home.HomeScreenViewModel
import com.android.readtracker.utils.formatDate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

@Composable
fun TrackerStatsScreen(
    navController: NavController,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {
    var books: List<MBook>
    val currentUser = FirebaseAuth.getInstance().currentUser

    Scaffold(
        topBar = {
            ReadTrackerTopBar(
                title = "Book Stats",
                icon = Icons.Default.ArrowBack,
                showProfile = false,
                navController = navController
            ) {
                navController.popBackStack()
            }
        }
    ) {

        Surface() {
            books = if (!viewModel.data.value.data.isNullOrEmpty()) {
                viewModel.data.value.data!!.filter { mBook ->
                    (mBook.userId == currentUser?.uid)
                }
            } else {
                emptyList()
            }

            Column {
                Row {
                    Box(
                        modifier = Modifier
                            .padding(start = 15.dp)
                            .size(45.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Sharp.Person,
                            contentDescription = "Person Icon"
                        )
                    }
                    Text(text = "Hi, ${currentUser?.email.toString().split("@")[0]}")
                }

                val readBooksList = books.filter { mBook ->
                    (mBook.userId == currentUser?.uid) && (mBook.finishedReading != null)
                }

                val readingBooks = books.filter { mBook ->
                    mBook.startedReading != null && mBook.finishedReading == null
                }

                showStatsCard(
                    readBooksList = readBooksList,
                    readingBooks = readingBooks,
                    currentUser = currentUser
                )

                if (viewModel.data.value.loading == true) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        LinearProgressIndicator()
                    }
                } else {
                    Divider()
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        items(items = readBooksList) { book ->
                            BookStatsRow(book = book, navController = navController)
                        }
                    }
                }

            }

        }
    }
}

@Composable
fun showStatsCard(
    readBooksList: List<MBook>,
    readingBooks: List<MBook>,
    currentUser: FirebaseUser?
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        shape = CircleShape,
        elevation = 5.dp
    ) {


        Column(
            modifier = Modifier.padding(start = 25.dp, top = 4.dp, bottom = 4.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(text = "Your Stats", style = MaterialTheme.typography.h5)
            Divider()
            Text(text = "You're reading: ${readingBooks.size} books.")
            Text(text = "You've read: ${readBooksList.size} books.")

        }
    }
}


@Composable
fun BookStatsRow(book: MBook, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(start = 6.dp, end = 6.dp, bottom = 6.dp)
            .clickable {
                navController.navigate(ReadTrackerScreens.DetailScreen.name + "/${book.googleBookId}")
            },
        elevation = 6.dp
    ) {
        Row(
            modifier = Modifier.padding(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val imageUrl = book.photoUrl
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
                modifier = Modifier.padding(start = 5.dp, top = 2.dp, bottom = 2.dp, end = 5.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = book.title.toString(),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    if (book.rating!! > 4) {
                        Spacer(modifier = Modifier.fillMaxHeight(0.8f))
                        Icon(
                            imageVector = Icons.Default.ThumbUp,
                            contentDescription = "",
                            tint = Color.Green.copy(alpha = 0.5f)
                        )
                    } else {
                        Box {}
                    }
                }

                Text(
                    text = "Author: ${book.authors ?: "Unknown"}",
                    overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.caption
                )
                Text(
                    text = "Started: ${book.startedReading?.let { formatDate(it) } ?: "Unknown"}",
                    softWrap = true,
                    overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.caption

                )
                Text(
                    text = "Finished: ${book.finishedReading?.let { formatDate(it) } ?: "[]"}",
                    overflow = TextOverflow.Ellipsis,
                    fontStyle = FontStyle.Italic,
                    maxLines = 1,
                    style = MaterialTheme.typography.caption

                )


            }

        }
    }
}