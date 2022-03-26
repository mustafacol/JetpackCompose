package com.mustafacol.jettip

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mustafacol.jettip.components.InputField
import com.mustafacol.jettip.ui.theme.JetTipTheme
import com.mustafacol.jettip.util.calculatePerPerson
import com.mustafacol.jettip.util.calculateTipAmount
import com.mustafacol.jettip.widgets.RoundIconButton

class MainActivity : ComponentActivity() {
    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetTipTheme {
                // A surface container using the 'background' color from the theme
                MyApp {
                    MainContent()
                }
            }
        }
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit) {
    Surface(color = MaterialTheme.colors.background) {

        content()
    }
}

@Composable
fun TopHeader(totalPerPerson: Double = 0.0) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .height(150.dp)
            .clip(shape = RoundedCornerShape(corner = CornerSize(12.dp))),
        color = Color(0xFFE8D1F6)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Total Per Person",
                style = MaterialTheme.typography.h4,

                )

            Text(
                text = "$${"%.2f".format(totalPerPerson)}",
                style = MaterialTheme.typography.h4,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

@Preview
@ExperimentalComposeUiApi
@Composable
fun MainContent() {
    val tipAmountState = remember {
        mutableStateOf(0.0)
    }
    val totalPerPersonState = remember {
        mutableStateOf(0.0)
    }
    val splitCountState = remember {
        mutableStateOf(1)
    }
    BillForm(
        splitCountState = splitCountState,
        tipAmountState = tipAmountState,
        totalPerPersonState = totalPerPersonState
    ) {
        Log.d("Hello", it)
    }
}

@ExperimentalComposeUiApi
@Composable
fun BillForm(
    modifier: Modifier = Modifier,
    splitCountState: MutableState<Int>,
    tipAmountState: MutableState<Double>,
    totalPerPersonState: MutableState<Double>,
    onValChange: (String) -> Unit = {}
) {
    val totalBillState = remember {
        mutableStateOf("")
    }
    val validState = remember(totalBillState.value) {
        totalBillState.value.trim().isNotEmpty()
    }

    val sliderPositionState = remember {
        mutableStateOf(0.0f)
    }
    val tipPercentageState = remember {
        mutableStateOf((sliderPositionState.value * 100).toInt())
    }

    val keyboardController = LocalSoftwareKeyboardController.current


    Column() {
        TopHeader(totalPerPerson = totalPerPersonState.value)
        Surface(
            modifier = modifier
                .padding(9.dp)
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(corner = CornerSize(8.dp))),
            border = BorderStroke(width = 2.dp, color = Color.LightGray)
        ) {

            Column {
                InputField(
                    valueState = totalBillState,
                    labelId = "Enter Bill",
                    enabled = true,
                    isSingleLine = true,
                    onAction = KeyboardActions {
                        if (!validState) return@KeyboardActions
                        onValChange(totalBillState.value.trim())
                        keyboardController?.hide()
                    })
                if (validState) {
                    Row(
                        modifier = modifier.padding(5.dp),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            text = "Split",
                            modifier = modifier.align(
                                alignment = Alignment.CenterVertically
                            )
                        )
                        Spacer(modifier = modifier.width(120.dp))

                        Row(
                            modifier = modifier.padding(horizontal = 3.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            RoundIconButton(
                                imageVector = Icons.Default.Remove,
                                backgroundColor = Color.White,
                                onClick = {
                                    if (splitCountState.value > 1)
                                        splitCountState.value--
                                    totalPerPersonState.value = calculatePerPerson(
                                        totalBill = totalBillState.value.toDouble(),
                                        tipPercentage = tipPercentageState.value,
                                        splitBy = splitCountState.value
                                    )
                                })
                            Text(
                                modifier = modifier
                                    .align(Alignment.CenterVertically)
                                    .padding(start = 9.dp, end = 9.dp),
                                text = splitCountState.value.toString()
                            )
                            RoundIconButton(
                                imageVector = Icons.Default.Add,
                                backgroundColor = Color.White,
                                onClick = {
                                    splitCountState.value++
                                    totalPerPersonState.value = calculatePerPerson(
                                        totalBill = totalBillState.value.toDouble(),
                                        tipPercentage = tipPercentageState.value,
                                        splitBy = splitCountState.value
                                    )
                                })
                        }


                    }

                    Row(modifier = modifier.padding(horizontal = 5.dp, vertical = 12.dp)) {
                        Text(
                            text = "Tip",
                            modifier = modifier.align(alignment = CenterVertically)
                        )
                        Spacer(modifier = modifier.width(200.dp))
                        Text(
                            text = "$${tipAmountState.value}",
                            modifier = modifier.align(alignment = CenterVertically)
                        )


                    }

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "${tipPercentageState.value}%")
                        Spacer(modifier = modifier.height(14.dp))
                        Slider(
                            value = sliderPositionState.value, onValueChange = { newVal ->
                                sliderPositionState.value = newVal
                                tipPercentageState.value = (sliderPositionState.value * 100).toInt()


                                tipAmountState.value = calculateTipAmount(
                                    totalBill = totalBillState.value.toDouble(),
                                    tipPercentage = tipPercentageState.value
                                )

                                totalPerPersonState.value = calculatePerPerson(
                                    totalBill = totalBillState.value.toDouble(),
                                    tipPercentage = tipPercentageState.value,
                                    splitBy = splitCountState.value
                                )


                            }, modifier = modifier.padding(end = 16.dp, start = 16.dp),
                            steps = 5
                        )
                    }
                } else {
                    Box {}
                }


            }
        }
    }


}
