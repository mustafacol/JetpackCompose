package com.android.readtracker.screens.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import  com.android.readtracker.R
import com.android.readtracker.components.ReadTrackerTopBar
import com.android.readtracker.model.MBook
import com.android.readtracker.model.MUser
import com.android.readtracker.navigation.ReadTrackerScreens
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun Home(navController: NavController = NavController(LocalContext.current)) {
    Scaffold(
        topBar = {
            ReadTrackerTopBar(title = "Read Tracker", navController = navController)
        },
        floatingActionButton = {
            FABContent {}
        }
    ) {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {

            HomeContent(navController = navController)
        }
    }
}

@Composable
fun HomeContent(navController: NavController = NavController(LocalContext.current)) {

    val email = FirebaseAuth.getInstance().currentUser?.email

    val currentUserName = if (!email.isNullOrEmpty()) email.split("@")[0] else "N/A"

//    FirebaseFirestore.getInstance().collection("users").whereEqualTo("user_id", currentUserId).get()
//        .addOnSuccessListener {
//           val user= it.documents[0].toObject(MUser::class.java)
//            Log.d("User", user?.firstName.toString())
//        }


    Column(
        modifier = Modifier.padding(2.dp),
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


            }
        }
        ListCard()


    }
}

@Composable
fun TitleSection(
    modifier: Modifier = Modifier,
    label: String
) {
    Text(
        modifier = modifier.padding(3.dp),
        text = label,
        fontSize = 19.sp,
        textAlign = TextAlign.Left
    )
}

@Composable
fun ReadingRightNowArea(books: List<MBook>, navController: NavController) {

}


@Composable
fun FABContent(onTap: () -> Unit = {}) {
    FloatingActionButton(
        onClick = { /*TODO*/ },
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

@Preview
@Composable
fun ListCard(
    book: MBook = MBook("asfas", "Flutter in Action", "Eric Windmill", "Hello world"),
    onPress: (String) -> Unit = {}
) {
    val context = LocalContext.current
    val resources = context.resources
    val displayMetrics = resources.displayMetrics

    val screenWidth = displayMetrics.widthPixels / displayMetrics.density

    val spacing = 10.dp
    Card(
        shape = RoundedCornerShape(25.dp),
        backgroundColor = Color.White,
        elevation = 6.dp,
        modifier = Modifier
            .padding(16.dp)
            .height(250.dp)
            .width(200.dp)
            .clickable { onPress.invoke(book.title.toString()) }
    ) {

        Column(
            modifier = Modifier.width(screenWidth.dp - (spacing * 2))
        ) {

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically

            ) {
                Image(
                    painter = rememberImagePainter(data = "http://books.google.com/books/content?id=Y5MqEAAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api"),
                    contentDescription = "book image",
                    modifier = Modifier
                        .height(140.dp)
                        .width(100.dp)
                        .padding(6.dp)
                )
                Spacer(modifier = Modifier.width(50.dp))

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite Icon"
                    )

                    Surface(
                        modifier = Modifier.padding(top = 2.dp),
                        color = Color.White,
                        shape = RoundedCornerShape(60),
                        elevation = 2.dp
                    ) {
                        Column(
                            modifier = Modifier.padding(4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.StarBorder,
                                contentDescription = "Star Icon"
                            )
                            Text(
                                text = "0.0",
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                        }
                    }
                }

            }

            Text(
                text = book.title.toString(),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
            )
            Text(text = "[${book.authors}]")


        }
        Row(horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.Bottom) {
            RoundedButton()
        }

    }
}

@Composable
fun RoundedButton(
    label: String = "Reading",
    radius: Int = 25,
    onPress: () -> Unit = {}
) {
    Surface(
        modifier = Modifier.clickable {
            onPress()
        },
        shape = RoundedCornerShape(topStart = radius.dp, bottomEnd = radius.dp),
        color = Color(0xFF8AC2D9)
    ) {
        Text(
            text = label,
            color = Color.White,
            modifier = Modifier.padding(6.dp)
        )
    }
}