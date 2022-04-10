package com.android.jetweatherforecast.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.android.jetweatherforecast.model.Unit
import com.android.jetweatherforecast.screens.settings.SettingsViewModel
import com.android.jetweatherforecast.widgets.WeatherAppBar

@Composable
fun SettingsScreen(
    navController: NavController,
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    val unitToggleState = remember { mutableStateOf(false) }
    val measurementUnits = listOf("Imperial (F)", "Metric (C)")
    val choiceFromDb = settingsViewModel.unitList.collectAsState().value
    val defaultChoice =
        if (choiceFromDb.isNullOrEmpty()) measurementUnits[0] else choiceFromDb[0].unit

    var choiceState by remember { mutableStateOf(defaultChoice) }
    Scaffold(
        topBar = {
            WeatherAppBar(
                title = "Settings",
                icon = Icons.Default.ArrowBack,
                isMainScreen = false,
                navController = navController
            ) {
                navController.popBackStack()
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Text(
                text = "Change Units of Measurement",
                style = MaterialTheme.typography.h6
            )
            IconToggleButton(
                checked = !unitToggleState.value,
                onCheckedChange = {
                    unitToggleState.value = !it
                    choiceState = if (unitToggleState.value) {
                        "Imperial (F)"
                    } else {
                        "Metric (C)"
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .clip(shape = RectangleShape)
                    .padding(5.dp)
                    .background(Color.Magenta.copy(alpha = 0.4f))
            ) {
                Text(text = if (unitToggleState.value) "Fahrenheit" else "Celsius")
            }
            Button(
                modifier = Modifier
                    .padding(3.dp)
                    .align(CenterHorizontally),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFFE9B346)
                ),
                onClick = {
                    settingsViewModel.deleteAllUnits()
                    settingsViewModel.insertUnit(Unit(unit = choiceState))
                }
            ) {
                Text(text = "Save", color = Color.White, fontSize = 16.sp)
            }
        }
    }
}