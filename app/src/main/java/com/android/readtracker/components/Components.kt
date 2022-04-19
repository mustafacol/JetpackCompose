package com.android.readtracker.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.android.readtracker.R
import com.android.readtracker.model.MBook
import com.android.readtracker.navigation.ReadTrackerScreens
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ReadTrackerLogo(modifier: Modifier = Modifier) {
    Text(
        modifier = modifier,
        text = "Read Tracker", style = MaterialTheme.typography.h3,
        color = Color.Red.copy(alpha = 0.6f)
    )
}

@Composable
fun EmailInput(
    modifier: Modifier = Modifier,
    emailState: MutableState<String>,
    label: String = "Email",
    enabled: Boolean = true,
    imeAction: ImeAction = ImeAction.Next,
    onAction: KeyboardActions = KeyboardActions.Default
) {
    InputField(
        modifier = modifier,
        valueState = emailState,
        label = label,
        enabled = true,
        isSingleLine = true,
        keyboardType = KeyboardType.Email,
        imeAction = imeAction,
        onAction = onAction
    )
}

@Composable
fun InputField(
    modifier: Modifier = Modifier,
    valueState: MutableState<String>,
    label: String,
    enabled: Boolean,
    isSingleLine: Boolean,
    activeSearch: Boolean = false,
    valueChange: (String) -> Unit = {},
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    onAction: KeyboardActions = KeyboardActions.Default
) {
    OutlinedTextField(
        value = valueState.value,
        onValueChange = {
            valueState.value = it
            if (activeSearch) {
                valueChange(valueState.value)
            }
        },
        label = { Text(text = label) },
        enabled = enabled,
        singleLine = isSingleLine,
        textStyle = TextStyle(
            fontSize = 18.sp,
            color = MaterialTheme.colors.onBackground
        ),
        modifier = modifier
            .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
            .fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction,
        ),
        keyboardActions = onAction
    )
}

@Composable
fun FirstNameInput(
    modifier: Modifier = Modifier,
    firstNameState: MutableState<String>,
    label: String = "First Name",
    enabled: Boolean = true,
    imeAction: ImeAction = ImeAction.Next,
    onAction: KeyboardActions = KeyboardActions.Default
) {
    InputField(
        modifier = modifier,
        valueState = firstNameState,
        label = label,
        enabled = true,
        isSingleLine = true,
        keyboardType = KeyboardType.Text,
        imeAction = imeAction,
        onAction = onAction
    )
}

@Composable
fun LastNameInput(
    modifier: Modifier = Modifier,
    lastNameState: MutableState<String>,
    label: String = "Last Name",
    enabled: Boolean = true,
    imeAction: ImeAction = ImeAction.Next,
    onAction: KeyboardActions = KeyboardActions.Default
) {
    InputField(
        modifier = modifier,
        valueState = lastNameState,
        label = label,
        enabled = true,
        isSingleLine = true,
        keyboardType = KeyboardType.Text,
        imeAction = imeAction,
        onAction = onAction
    )
}


@Composable
fun PasswordInput(
    modifier: Modifier,
    passwordState: MutableState<String>,
    label: String,
    enabled: Boolean,
    passwordVisibility: MutableState<Boolean>,
    imeAction: ImeAction = ImeAction.Done,
    onAction: KeyboardActions = KeyboardActions.Default
) {

    val visualTransformation =
        if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation()

    OutlinedTextField(
        value = passwordState.value,
        onValueChange = { passwordState.value = it },
        label = { Text(text = label) },
        singleLine = true,
        textStyle = TextStyle(fontSize = 18.sp, color = MaterialTheme.colors.onBackground),
        modifier = modifier
            .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
            .fillMaxWidth(),
        enabled = enabled,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = imeAction
        ),
        visualTransformation = visualTransformation,
        trailingIcon = { PasswordVisibility(passwordVisibility) },
        keyboardActions = onAction
    )
}

@Composable
fun PasswordVisibility(passwordVisibility: MutableState<Boolean>) {
    val visible = passwordVisibility.value
    IconButton(onClick = { passwordVisibility.value = !visible }) {
        //Icon(imageVector = Icons.Default.Close, contentDescription = null)
        if (visible) Icon(
            painter = painterResource(R.drawable.outline_visibility_off_black_24),
            contentDescription = ""
        ) else Icon(
            painter = painterResource(R.drawable.outline_visibility_black_24),
            contentDescription = ""
        )
        //Image(painter = painterResource(id = R.mipmap.) , contentDescription = null)
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SubmitButton(
    text: String,
    loading: Boolean,
    validInputs: Boolean,
    keyboardController: SoftwareKeyboardController?,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        enabled = !loading && validInputs,
        shape = RoundedCornerShape(20)
    ) {
        if (loading) CircularProgressIndicator(modifier = Modifier.size(25.dp))
        else Text(text = text, modifier = Modifier.padding(5.dp))
        keyboardController?.hide()
    }
}

@Composable
fun ReadTrackerTopBar(
    title: String,
    icon: ImageVector? = null,
    showProfile: Boolean = true,
    navController: NavController,
    onBackArrowClicked: () -> Unit = {}
) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (showProfile) {
                    Icon(
                        painterResource(id = R.drawable.ic_baseline_account_circle_24),
                        contentDescription = "",
                        modifier = Modifier.size(45.dp)
                    )

                }
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = "Arrow Back",
                        tint = Color.Red.copy(alpha = 0.7f),
                        modifier = Modifier.clickable { onBackArrowClicked.invoke() }
                    )
                }
                Text(
                    modifier = Modifier.padding(start = 6.dp),
                    text = title,
                    color = Color.Red.copy(alpha = 0.6f),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold

                )


            }
        },
        actions = {
            if (showProfile) {
                IconButton(onClick = {
                    FirebaseAuth.getInstance().signOut().run {
                        navController.navigate(ReadTrackerScreens.LoginScreen.name)
                    }
                }) {
                    Icon(imageVector = Icons.Filled.Logout, contentDescription = "")
                }
            }

        },

        backgroundColor = Color.Transparent,
        elevation = 1.dp,
        modifier = Modifier.padding(15.dp)

    )
}

@Composable
fun BookRating(score: Double = 4.5) {
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
                text = score.toString(),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
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
    val isStartedReading = remember {
        mutableStateOf(false)
    }
    val spacing = 10.dp
    Card(
        shape = RoundedCornerShape(25.dp),
        backgroundColor = Color.White,
        elevation = 6.dp,
        modifier = Modifier
            .padding(16.dp)
            .height(240.dp)
            .width(200.dp)
            .clickable { onPress.invoke(book.id.toString()) }
    ) {

        Column(
            modifier = Modifier.width(screenWidth.dp - (spacing * 2)),
            horizontalAlignment = Alignment.Start

        ) {

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically

            ) {
                Image(
                    painter = rememberImagePainter(
                        book.photoUrl
                            ?: "https://upload.wikimedia.org/wikipedia/commons/thumb/5/50/Closed_Book_Icon.svg/512px-Closed_Book_Icon.svg.png"
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .height(140.dp)
                        .width(100.dp)
                        .padding(4.dp)
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
                    BookRating(book.rating ?: 0.0)


                }

            }

            Text(
                modifier = Modifier.padding(4.dp),
                text = book.title.toString(),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                maxLines = 2
            )
            Text(
                modifier = Modifier.padding(4.dp),
                text = "${book.authors}".replace("[", "").replace("]", ""),
                maxLines = 2
            )
        }

        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.Bottom
        ) {
            isStartedReading.value = book.startedReading != null
            RoundedButton(
                label = if (isStartedReading.value) "Reading" else "Not Started"
            )
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

@Composable
fun CustomRoundedButton(
    label: String = "Reading",
    radius: Int = 25,
    onPress: () -> Unit = {}
) {
    Surface(
        modifier = Modifier
            .width(80.dp)
            .clickable {
                onPress()
            },
        shape = RoundedCornerShape(radius.dp),
        color = Color(0xFF8AC2D9)
    ) {
        Text(
            text = label,
            color = Color.White,
            modifier = Modifier.padding(6.dp),
            textAlign = TextAlign.Center
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SimpleForm(
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    defaultValue: MutableState<String>,
    onSearch: (String) -> Unit
) {
    //val textFieldValue = rememberSaveable { mutableStateOf(defaultValue) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val valid = remember(defaultValue.value) {
        defaultValue.value.trim().isNotEmpty()
    }

    InputField(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.3f)
            .padding(4.dp)
            .background(color = Color.White, shape = RoundedCornerShape(25)),
        valueState = defaultValue,
        valueChange = {
            defaultValue.value = it
        },
        label = "Your thoughts",
        enabled = !loading,
        isSingleLine = false,
        onAction = KeyboardActions {
            if (!valid) return@KeyboardActions
            onSearch(defaultValue.value.trim())
            keyboardController?.hide()
        }
    )
}