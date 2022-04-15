package com.android.readtracker.screens.home

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.android.readtracker.components.ListCard
import com.android.readtracker.components.ReadTrackerTopBar
import com.android.readtracker.model.MBook
import com.android.readtracker.navigation.ReadTrackerScreens
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Home(
    navController: NavController,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            ReadTrackerTopBar(title = "Read Tracker", navController = navController)
        },
        floatingActionButton = {
            FABContent {
                navController.navigate(ReadTrackerScreens.SearchScreen.name)
            }
        }
    ) {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {

            HomeContent(navController = navController, viewModel = viewModel)
        }
    }
}

@Composable
fun HomeContent(
    navController: NavController = NavController(LocalContext.current),
    viewModel: HomeScreenViewModel
) {

//    val listOfBooks = listOf(
//        MBook(id = "1", title = "Hello World", authors = "You", notes = null),
//        MBook(id = "2", title = "Hello World", authors = "You", notes = null),
//        MBook(id = "3", title = "Hello World", authors = "You", notes = null),
//        MBook(id = "4", title = "Hello World", authors = "You", notes = null),
//        MBook(id = "5", title = "Hello World", authors = "You", notes = null),
//
//        )

    var listOfBooks = emptyList<MBook>()
    val currentUser = FirebaseAuth.getInstance().currentUser

    if (!viewModel.data.value.data.isNullOrEmpty()) {
        listOfBooks = viewModel.data.value.data!!.toList().filter { mBook ->
            mBook.userId == currentUser?.uid.toString()
        }

        Log.d("Books", "HomeContent: ${listOfBooks.toString()}")
    }
    val email = FirebaseAuth.getInstance().currentUser?.email

    val currentUserName = if (!email.isNullOrEmpty()) email.split("@")[0] else "N/A"

//    FirebaseFirestore.getInstance().collection("users").whereEqualTo("user_id", currentUserId).get()
//        .addOnSuccessListener {
//           val user= it.documents[0].toObject(MUser::class.java)
//            Log.d("User", user?.firstName.toString())
//        }


    Column(
        modifier = Modifier
            .padding(2.dp)
            .verticalScroll(state = rememberScrollState()),
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier
                .align(alignment = Alignment.Start)
                .padding(start = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TitleSection(label = "Your reading \n" + "activity right now...")

            Spacer(modifier = Modifier.fillMaxWidth(0.7f))

            Column {
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "",
                    modifier = Modifier
                        .clickable {
                            navController.navigate(ReadTrackerScreens.ReaderStatsScreen.name)
                        }
                        .size(45.dp)
                        .align(Alignment.CenterHorizontally),
                    tint = MaterialTheme.colors.secondaryVariant
                )
                Text(
                    modifier = Modifier
                        .padding(2.dp)
                        .align(Alignment.CenterHorizontally),
                    text = currentUserName,
                    fontSize = 17.sp,
                    color = Color.Red.copy(alpha = 0.7f),
                    maxLines = 1,
                )

                Divider()

            }
        }

        ReadingRightNowArea(books = listOf(), navController = navController)

        TitleSection(label = "Reading List")

        BookListArea(listOfBooks = listOfBooks, navController = navController)

    }
}


@Composable
fun BookListArea(listOfBooks: List<MBook>, navController: NavController) {

    HorizontalScrollableComponent(listOfBooks = listOfBooks) {
        navController.navigate(ReadTrackerScreens.UpdateScreen.name + "/$it")
    }
}

@Composable
fun HorizontalScrollableComponent(listOfBooks: List<MBook>, onCardPressed: (String) -> Unit) {
    val scrollState = rememberScrollState()

    LazyRow {
        items(listOfBooks) { book ->
            ListCard(book = book) {
                onCardPressed(book.googleBookId.toString())
            }
        }
    }
}

@Composable
fun TitleSection(
    modifier: Modifier = Modifier,
    label: String
) {
    Text(
        modifier = modifier.padding(start = 16.dp),
        text = label,
        fontSize = 19.sp,
        textAlign = TextAlign.Left
    )
}

@Composable
fun ReadingRightNowArea(books: List<MBook>, navController: NavController) {
    ListCard()
}


@Composable
fun FABContent(onTap: () -> Unit = {}) {
    FloatingActionButton(
        onClick = {
            onTap()
        },
        shape = RoundedCornerShape(50),
        backgroundColor = Color(0xFF8AC2D9)
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Icon",
            tint = Color.White

        )
    }
}



