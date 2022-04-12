package com.android.readtracker.screens.login

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.android.readtracker.components.*
import com.android.readtracker.navigation.ReadTrackerScreens

@Composable
fun TrackerLoginScreen(
    navController: NavHostController,
    viewModel: LoginScreenViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {

    val showLoginForm = rememberSaveable { mutableStateOf(true) }
    Surface(
        modifier = Modifier.fillMaxSize()

    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            ReadTrackerLogo(modifier = Modifier.padding(20.dp))
            if (showLoginForm.value)
                UserForm(
                    loading = false,
                    isCreateAccount = !showLoginForm.value,
                ) { email, password,firstname,lastname ->
                    //Log.d("Form", "ReaderLoginScreen $email, $password")

                    viewModel.signInWithEmailAndPassword(email, password) {
                        navController.navigate(ReadTrackerScreens.ReadTrackerHomeScreen.name)
                    }
                }
            else {
                Text(
                    modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 15.dp),
                    text = "Please enter a valid email and pass word that is at least 6 characters."
                )
                UserForm(loading = false, isCreateAccount = true) { email, password,firstname,lastname ->
                    viewModel.createUserWithEmailAndPassword(email, password,firstname,lastname) {
                        navController.navigate(ReadTrackerScreens.ReadTrackerHomeScreen.name)
                    }
                }
            }

            Row(
                modifier = Modifier.padding(15.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val actionText = if (showLoginForm.value) "Sign Up" else "Login"
                Text(text = if (showLoginForm.value) "New User ?" else "Exist Account ?")
                Text(
                    text = actionText,
                    modifier = Modifier
                        .clickable {
                            showLoginForm.value = !showLoginForm.value
                        }
                        .padding(5.dp),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.secondaryVariant
                )


            }
        }


    }

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun UserForm(
    loading: Boolean = false,
    isCreateAccount: Boolean=false,
    onDone: (String, String, String?, String?) -> Unit = { email, psw, firstname, lastname -> }
) {
    val email = rememberSaveable {
        mutableStateOf("")
    }
    val password = rememberSaveable {
        mutableStateOf("")
    }
    val firstName = rememberSaveable {
        mutableStateOf("")
    }
    val lastName = rememberSaveable {
        mutableStateOf("")
    }
    val passwordVisibility = rememberSaveable {
        mutableStateOf(false)
    }
    val firstNameFocusRequest = FocusRequester()
    val lastNameFocusRequest = FocusRequester()
    val passwordFocusRequest = FocusRequester()

    val keyboardController = LocalSoftwareKeyboardController.current
    val valid = remember(email.value, password.value) {
        email.value.trim().isNotEmpty() && password.value.trim().isNotEmpty()
    }
    val firstAndLastNameValid = remember(firstName.value, lastName.value) {
        firstName.value.trim().isNotEmpty() && lastName.value.trim().isNotEmpty()
    }
    val modifier = Modifier
        .background(MaterialTheme.colors.background)
        .verticalScroll(
            rememberScrollState()
        )


    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EmailInput(
            emailState = email,
            enabled = !loading,
            onAction = KeyboardActions {
                if (isCreateAccount)
                    firstNameFocusRequest.requestFocus()
                else
                    passwordFocusRequest.requestFocus()
            }
        )
        if (isCreateAccount) {
            FirstNameInput(
                modifier = Modifier.focusRequester(firstNameFocusRequest),
                firstNameState = firstName,
                label = "First Name",
                enabled = !loading,
                imeAction = ImeAction.Next,
                onAction = KeyboardActions {
                    lastNameFocusRequest.requestFocus()
                }
            )
            LastNameInput(
                modifier = Modifier.focusRequester(lastNameFocusRequest),
                lastNameState = lastName,
                label = "Last Name",
                enabled = !loading,
                imeAction = ImeAction.Next,
                onAction = KeyboardActions {
                    passwordFocusRequest.requestFocus()
                }
            )

        }

        PasswordInput(
            modifier = Modifier.focusRequester(passwordFocusRequest),
            passwordState = password,
            label = "Password",
            enabled = !loading,
            passwordVisibility = passwordVisibility,
            imeAction = ImeAction.Done,
            onAction = KeyboardActions {
                keyboardController?.hide()
                if (!valid && !firstAndLastNameValid) return@KeyboardActions
                onDone(
                    email.value.trim(),
                    password.value.trim(),
                    if (isCreateAccount) firstName.value.trim() else null,
                    if (isCreateAccount) lastName.value.trim() else null
                )

            }
        )



        SubmitButton(
            text = if (isCreateAccount) "Create Account" else "Login",
            loading = loading,
            validInputs = if(isCreateAccount) valid && firstAndLastNameValid else valid,
            keyboardController = keyboardController
        ) {
            onDone(
                email.value.trim(),
                password.value.trim(),
                if (isCreateAccount) firstName.value.trim() else null,
                if (isCreateAccount) lastName.value.trim() else null
            )
        }

    }
}


