package com.android.readtracker.screens.update

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.android.readtracker.R
import com.android.readtracker.components.CustomRoundedButton
import com.android.readtracker.components.ReadTrackerTopBar
import com.android.readtracker.components.SimpleForm
import com.android.readtracker.data.DataOrException
import com.android.readtracker.model.MBook
import com.android.readtracker.navigation.ReadTrackerScreens
import com.android.readtracker.screens.home.HomeScreenViewModel
import com.android.readtracker.utils.formatDate
import com.google.firebase.Timestamp
import com.mahmoudalim.compose_rating_bar.RatingBarView

@Composable
fun TrackerUpdateScreen(
    navController: NavController,
    bookItemId: String,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    Scaffold(
        topBar = {
            ReadTrackerTopBar(
                title = "Update Book",
                navController = navController,
                icon = Icons.Default.ArrowBack,
                showProfile = false,
            ) {
                navController.popBackStack()
            }
        }
    ) {

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(3.dp),


            ) {
            Column(
                modifier = Modifier.padding(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                val bookInfo =
                    produceState<DataOrException<List<MBook>, Boolean, Exception>>(
                        initialValue = DataOrException(listOf(), true, Exception("")),
                    ) {
                        value = viewModel.data.value
                    }.value


                if (bookInfo.loading == true) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                        Text(text = "Loading...")
                        bookInfo.loading = false
                    }
                } else {

                    ShowBookUpdate(viewModel.data.value, bookItemId, navController, viewModel)

                }
            }
        }

    }
}

@Composable
fun ShowBookUpdate(
    bookInfo: DataOrException<List<MBook>, Boolean, Exception>,
    bookItemId: String,
    navController: NavController,
    viewModel: HomeScreenViewModel
) {


    val bookItem = bookInfo.data?.first { mBook ->
        mBook.googleBookId == bookItemId
    }

    if (bookItem != null) {

        val ratingState = remember(bookItem) {
            mutableStateOf(bookItem.rating?.toInt() ?: 0)
        }

        val noteTextState = remember {
            mutableStateOf(if (bookItem.notes != null) bookItem.notes.toString() else "")
        }

        val isFinishedReading = remember {
            mutableStateOf(false)
        }
        val isStartedReading = remember {
            mutableStateOf(false)
        }

        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ShowBookCard(bookItem = bookItem)
            ShowSimpleForm(book = bookItem, noteTextState = noteTextState)
            ShowReadingButtons(book = bookItem, isStartedReading, isFinishedReading)
            ShowRatingBar(ratingState = ratingState)
            Spacer(modifier = Modifier.padding(bottom = 15.dp))


            ShowUpdateDeleteButtons(
                bookItem,
                ratingState,
                noteTextState,
                isStartedReading,
                isFinishedReading,
                navController,
                viewModel
            )
        }

    }

}

@Composable
fun ShowBookCard(bookItem: MBook) {

    Surface(
        modifier = Modifier
            .padding(2.dp)
            .fillMaxWidth(),
        shape = CircleShape,
        elevation = 4.dp
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = rememberImagePainter(
                    bookItem.photoUrl
                        ?: "https://upload.wikimedia.org/wikipedia/commons/thumb/5/50/Closed_Book_Icon.svg/512px-Closed_Book_Icon.svg.png"
                ),
                contentDescription = null,
                modifier = Modifier
                    .height(120.dp)
                    .width(100.dp)
                    .padding(10.dp)
                    .weight(1f)

            )

            Column(
                modifier = Modifier
                    .padding(5.dp)
                    .weight(1f),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center

            ) {
                Text(
                    text = "${bookItem.title}",
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = bookItem.authors?.replace("[", "")?.replace("]", "") ?: "UnKnown",
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.caption
                )
                Text(
                    text = bookItem.publishedDate ?: "Unknown",
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.caption
                )
            }
        }


    }


}

@Composable
fun ShowSimpleForm(book: MBook, noteTextState: MutableState<String>) {

    SimpleForm(
        modifier = Modifier,
        defaultValue = noteTextState
    ) {
        noteTextState.value = it
    }
}

@Composable
fun ShowReadingButtons(
    book: MBook,
    isStartedReading: MutableState<Boolean>,
    isFinishedReading: MutableState<Boolean>
) {

    Row(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        TextButton(
            onClick = { isStartedReading.value = true },
            enabled = book.startedReading == null
        ) {
            if (book.startedReading == null) {
                Text(
                    text = if (!isStartedReading.value) "Start Reading" else "Started Reading",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold
                )
            } else {
                Text(text = "Started on: ${formatDate(book.startedReading!!)}")
            }

        }

        TextButton(
            onClick = { isFinishedReading.value = true },
            enabled = book.finishedReading == null

        ) {
            if (book.finishedReading == null) {
                Text(
                    text = if (!isFinishedReading.value) "Mark as Read" else "Finished Reading",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold
                )
            } else {
                Text(text = "Started on: ${formatDate(book.startedReading!!)}")

            }


        }
    }
}

@Composable
fun ShowRatingBar(ratingState: MutableState<Int>) {
    Column(
        modifier = Modifier.padding(5.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Rating",
            fontSize = 15.sp
        )
        RatingBarView(
            modifier = Modifier.padding(3.dp),
            rating = ratingState,
            isRatingEditable = true,
            numberOfStars = 5,
            isViewAnimated = true,
            starIcon = rememberVectorPainter(image = Icons.Default.Star),
            unRatedStarsColor = Color.LightGray,
            ratedStarsColor = Color(0xFFFFC73E)
        )
    }

}

@Composable
fun ShowUpdateDeleteButtons(
    book: MBook,
    ratingState: MutableState<Int>,
    noteTextState: MutableState<String>,
    isStartedReading: MutableState<Boolean>,
    isFinishedReading: MutableState<Boolean>,
    navController: NavController,
    viewModel: HomeScreenViewModel
) {
    val context = LocalContext.current.applicationContext

    val changedNotes = book.notes != noteTextState.value
    val changedRating = book.rating?.toInt() != ratingState.value
    val isFinishedTimeStamp = if (isFinishedReading.value) Timestamp.now() else book.finishedReading
    val isStartedTimeStamp = if (isStartedReading.value) Timestamp.now() else book.startedReading
    val bookUpdate =
        changedNotes || changedRating || isFinishedReading.value || isStartedReading.value

    val bookToUpdate = hashMapOf(
        "finished_reading" to isFinishedTimeStamp,
        "started_reading" to isStartedTimeStamp,
        "rating" to ratingState.value,
        "notes" to noteTextState.value
    ).toMap()


    val openDialog = remember {
        mutableStateOf(false)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceEvenly

    ) {
        CustomRoundedButton("Update") {
            if (bookUpdate) {
                viewModel.updateBook(
                    bookToUpdate = bookToUpdate,
                    bookId = book.id!!
                ) {

                    Toast.makeText(context, "Book is updated successfully.", Toast.LENGTH_SHORT)
                        .show()

                    showToast(context = context, message = "Book is updated successfully.")
                    navController.navigate(ReadTrackerScreens.ReadTrackerHomeScreen.name)
                }


            }
        }
        CustomRoundedButton(
            "Delete"
        ) {
            openDialog.value = true
        }
        if (openDialog.value) {
            ShowAlertDialog(
                title = stringResource(id = R.string.dialog_action),
                action = stringResource(id = R.string.dialog_action),
                openDialog = openDialog,
            ) {
                viewModel.deleteBook(
                    bookId = book.id!!,
                ) {
                    showToast(context = context, message = "Book is deleted successfully")
                    navController.navigate(ReadTrackerScreens.ReadTrackerHomeScreen.name)
                }

            }
        }

    }
}

@Composable
fun ShowAlertDialog(
    title: String,
    action: String,
    openDialog: MutableState<Boolean>,
    deleteBook: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { openDialog.value = false },
        title = { Text(text = title) },
        text = { Text(text = action) },
        confirmButton = {
            Button(
                onClick = {
                    deleteBook.invoke()
                    openDialog.value = false
                },
            ) {
                Text(text = "Yes")
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    openDialog.value = false
                }) {
                Text(text = "No")
            }
        }
    )


}

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}