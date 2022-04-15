package com.android.readtracker.screens.update

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.android.readtracker.components.CustomRoundedButton
import com.android.readtracker.components.InputField
import com.android.readtracker.components.ReadTrackerTopBar
import com.android.readtracker.data.DataOrException
import com.android.readtracker.model.MBook
import com.android.readtracker.screens.home.HomeScreenViewModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.mahmoudalim.compose_rating_bar.RatingBarView

@Composable
fun TrackerUpdateScreen(
    navController: NavController,
    bookItemId: String,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {

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
                    ShowBookUpdate(viewModel.data.value, bookItemId, navController)

                }
            }
        }

    }
}

@Composable
fun ShowBookUpdate(
    bookInfo: DataOrException<List<MBook>, Boolean, Exception>,
    bookItemId: String,
    navController: NavController
) {


    val bookItem = bookInfo.data?.first { mBook ->
        mBook.googleBookId == bookItemId
    }

    if (bookItem != null) {

        val ratingState = remember(bookItem) {
            mutableStateOf(bookItem?.rating?.toInt() ?: 0)
        }

        val noteTextState = remember {
            mutableStateOf("")
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
                isFinishedReading
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
                    text = "${
                        bookItem.authors?.replace("[", "")?.replace("]", "") ?: "UnKnown"
                    }",
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
        defaultValue = noteTextState.value
    ) {
        noteTextState.value = it
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SimpleForm(
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    defaultValue: String = "Hello World!!",
    onSearch: (String) -> Unit
) {
    val textFieldValue = rememberSaveable { mutableStateOf(defaultValue) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val valid = remember(textFieldValue.value) {
        textFieldValue.value.trim().isNotEmpty()
    }

    InputField(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.3f)
            .padding(4.dp)
            .background(color = Color.White, shape = RoundedCornerShape(25)),
        valueState = textFieldValue,
        valueChange = {
            textFieldValue.value = it
        },
        label = "Your thoughts",
        enabled = !loading,
        isSingleLine = false,
        onAction = KeyboardActions {
            if (!valid) return@KeyboardActions
            onSearch(textFieldValue.value.trim())
            keyboardController?.hide()
        }
    )
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
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        TextButton(
            onClick = { isStartedReading.value = !isStartedReading.value },
            enabled = book.startedReading == null
        ) {
            if (book.startedReading == null) {
                Text(
                    text = if (!isStartedReading.value) "Start Reading" else "Started Reading",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold
                )
            } else {
                Text(text = "Started on: ${book.startedReading}")
            }

        }

        TextButton(
            onClick = { isFinishedReading.value = !isFinishedReading.value },
            enabled = book.finishedReading == null

        ) {
            if (book.finishedReading == null) {
                Text(
                    text = if (!isFinishedReading.value) "Mark as Read" else "Finished Reading",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold
                )
            } else {
                Text(text = "Started on: ${book.startedReading}")

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
) {
    val changedNotes = book.notes != noteTextState.value
    val changedRating = book.rating?.toInt() != ratingState.value
    val isFinishedTimeStamp =
        if (isFinishedReading.value) Timestamp.now() else book.finishedReading
    val isStartedTimeStamp =
        if (isStartedReading.value) Timestamp.now() else book.startedReading

    val bookUpdate =
        changedNotes || changedRating || isFinishedReading.value || isStartedReading.value

    val bookToUpdate = hashMapOf(
        "finished_reading" to isFinishedTimeStamp,
        "started_reading" to isStartedTimeStamp,
        "rating" to ratingState.value,
        "notes" to noteTextState.value
    ).toMap()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceEvenly

    ) {
        CustomRoundedButton("Update") {
            if (bookUpdate) {
                FirebaseFirestore
                    .getInstance()
                    .collection("books")
                    .document(book.id!!)
                    .update(bookToUpdate)
                    .addOnCompleteListener {
                        Log.d("UpdateSuccess", "UpdateResult: ${it.result.toString()}")
                    }
                    .addOnFailureListener {
                        Log.w("UpdateFail", "Error updating document $it")
                    }
            }
        }
        CustomRoundedButton(
            "Delete"
        )

    }
}