package com.android.readtracker.screens.details

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.HtmlCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.android.readtracker.components.CustomRoundedButton
import com.android.readtracker.components.ReadTrackerTopBar
import com.android.readtracker.components.RoundedButton
import com.android.readtracker.data.Resource
import com.android.readtracker.model.Item
import com.android.readtracker.model.MBook
import com.android.readtracker.navigation.ReadTrackerScreens
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun BookDetailScreen(
    navController: NavController,
    viewModel: BookDetailsViewModel = hiltViewModel(),
    bookId: String
) {

    Scaffold(
        topBar = {
            ReadTrackerTopBar(
                title = "Book Details",
                showProfile = false,
                icon = Icons.Default.ArrowBack,
                navController = navController
            ) {
                navController.navigate(ReadTrackerScreens.SearchScreen.name)
            }
        }
    ) {
        Surface(
            modifier = Modifier
                .padding(3.dp)
                .fillMaxSize()
        ) {
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val bookInfo = produceState<Resource<Item>>(initialValue = Resource.Loading()) {
                    value = viewModel.getBookInfo(bookId = bookId)
                }.value

                if (bookInfo.data == null) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                } else {
                    ShowBookDetails(bookInfo = bookInfo, navController = navController)
                }
            }
        }
    }

}

@Composable
fun ShowBookDetails(
    bookInfo: Resource<Item>,
    navController: NavController
) {
    val bookData = bookInfo.data?.volumeInfo
    val googleBookId = bookInfo.data?.id

    Card(
        modifier = Modifier.padding(25.dp),
        shape = RoundedCornerShape(50),
        elevation = 0.dp
    ) {
        Image(
            painter = rememberImagePainter(
                data = bookData!!.imageLinks?.thumbnail
                    ?: "https://upload.wikimedia.org/wikipedia/commons/thumb/5/50/Closed_Book_Icon.svg/512px-Closed_Book_Icon.svg.png"
            ),
            contentDescription = "Book Image",
            modifier = Modifier
                .width(130.dp)
                .height(130.dp)
                .padding(2.dp)

        )
    }

    Text(
        text = bookData?.title.toString(),
        style = MaterialTheme.typography.h6,
        overflow = TextOverflow.Ellipsis
    )
    Text(
        text = bookData?.authors.toString().replace("[", "").replace("]", ""),
        style = MaterialTheme.typography.caption,
        fontSize = 17.sp,
        fontWeight = FontWeight.SemiBold,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.padding(top = 3.dp)
    )

    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(5.dp)
            .background(Color(0xFF8AC2D9))
            .fillMaxWidth(0.8f)
            .height(IntrinsicSize.Max)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = bookData?.pageCount.toString(),
                color = Color.White
            )
            Text(
                text = "Number of Page",
                color = Color.White
            )
        }
        Divider(
            modifier = Modifier
                .fillMaxHeight(0.7f)
                .width(1.dp),
            color = Color.White

        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.weight(1f)

        ) {
            Text(
                text = bookData?.publishedDate ?: "Unknown",
                color = Color.White
            )
            Text(
                text = "Published",
                color = Color.White
            )
        }

    }


    Text(
        text = "Categories:",
        modifier = Modifier
            .padding(6.dp)
            .fillMaxWidth(),
        textAlign = TextAlign.Start,
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold
    )
    if (bookData?.categories != null) {
        LazyRow {
            items(
                bookData.categories
            ) {
                CategoryItem(categoryName = it)
            }
        }
    }

    val cleanDescription = HtmlCompat.fromHtml(
        bookData!!.description,
        HtmlCompat.FROM_HTML_MODE_LEGACY
    ).toString()

    val localDims = LocalContext.current.resources.displayMetrics

    Surface(
        modifier = Modifier
            .height(localDims.heightPixels.dp.times(0.09f))
            .padding(4.dp),
        shape = RoundedCornerShape(10),
        border = BorderStroke(1.dp, Color.DarkGray)
    ) {
        LazyColumn {
            item {
                Text(
                    text = cleanDescription,
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
    }

    Row(
        modifier = Modifier
            .padding(top = 5.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly

    ) {

        CustomRoundedButton(
            label = "Save",
            radius = 10
        ) {
            val book = MBook(
                title = bookData.title,
                authors = bookData.authors.toString(),
                description = bookData.description,
                categories = bookData.categories.toString(),
                notes = "",
                photoUrl = bookData.imageLinks?.thumbnail ?: "",
                publishedDate = bookData.publishedDate,
                pageCount = bookData.pageCount.toString(),
                rating = 0.0,
                googleBookId = googleBookId,
                userId = FirebaseAuth.getInstance().currentUser?.uid
            )

            saveToFirebase(book, navController)

        }
        CustomRoundedButton(
            label = "Cancel",
            radius = 10
        ){
            navController.popBackStack()
        }

    }


}

@Composable
fun CategoryItem(
    categoryName: String
) {
    Surface(
        modifier = Modifier
            .padding(6.dp)
            .height(25.dp),
        shape = RoundedCornerShape(30),
        color = Color(0xFF8AC2D9)
    ) {
        Text(
            modifier = Modifier.padding(5.dp),
            text = categoryName,
            color = Color.White,
            style = MaterialTheme.typography.caption
        )
    }
}

fun saveToFirebase(book: MBook, navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    val dbCollection = db.collection("books")

    if (book.toString().isNotEmpty()) {
        dbCollection.add(book).addOnSuccessListener { documentRef ->
            val docId = documentRef.id
            dbCollection.document(docId)
                .update(hashMapOf("id" to docId) as Map<String, Any>)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        navController.popBackStack()
                    }
                }
                .addOnFailureListener {
                    Log.w("FirebaseUpdateError", "SaveToFirebase: Error updating doc", it)
                }

        }
    } else {

    }
}