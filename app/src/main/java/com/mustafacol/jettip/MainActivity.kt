package com.mustafacol.jettip

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.MaterialTheme
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mustafacol.jettip.components.InputField
import com.mustafacol.jettip.ui.theme.JetTipTheme
import com.mustafacol.jettip.widgets.RoundIconButton

class MainActivity : ComponentActivity() {
    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetTipTheme {
                // A surface container using the 'background' color from the theme
                MyApp {
                    //TopHeader()
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

@ExperimentalComposeUiApi
@Composable
fun MainContent() {
    BillForm {
        Log.d("Hello", it)
    }
}

@Preview
@ExperimentalComposeUiApi
@Composable
fun BillForm(modifier: Modifier = Modifier, onValChange: (String) -> Unit = {}) {
    val totalBillState = remember {
        mutableStateOf("")
    }
    val validState = remember(totalBillState.value) {
        totalBillState.value.trim().isNotEmpty()
    }
    val splitCount = remember {
        mutableStateOf(1)
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    Surface(
        modifier = Modifier
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
            }
            Row(modifier = Modifier.padding(5.dp), horizontalArrangement = Arrangement.Start) {
                Text(
                    text = "Split",
                    modifier = Modifier.align(
                        alignment = Alignment.CenterVertically
                    )
                )
                Spacer(modifier = Modifier.width(120.dp))

                Row(
                    modifier = Modifier.padding(horizontal = 3.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    RoundIconButton(
                        imageVector = Icons.Default.Remove,
                        backgroundColor = Color.White,
                        onClick = {
                            if (splitCount.value > 1)
                                splitCount.value--

                        })
                    Text(
                        modifier = modifier
                            .align(Alignment.CenterVertically)
                            .padding(start = 9.dp, end = 9.dp),
                        text = splitCount.value.toString()
                    )
                    RoundIconButton(
                        imageVector = Icons.Default.Add,
                        backgroundColor = Color.White,
                        onClick = {
                            splitCount.value++
                        })
                }


            }

            Row(){
                Text(text = "Hello",Modifier.weight(1f),Modifier.align( alignment = Alignment.CenterVertically))
                Text(text = "World",Modifier.weight(1f),Modifier.align( alignment = Alignment.CenterVertically))
            }
        }
    }
}
