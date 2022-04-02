package com.mustafacol.jettrivia.component

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mustafacol.jettrivia.screens.QuestionsViewModel
import com.mustafacol.jettrivia.util.AppColors

@Composable
fun Questions(viewModel: QuestionsViewModel) {
    val questions = viewModel.data.value.data?.toMutableList()

    if (viewModel.data.value.loading == true) {
        CircularProgressIndicator()
    } else {
        questions?.forEach { questionItem ->
            Log.d("Result", "Question: ${questionItem.question}}")
        }
    }
}

@Preview
@Composable
fun QuestionDisplay() {
    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    Surface(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(4.dp),
        color = AppColors.mDarkPurple
    ) {

        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            QuestionTracker()
            Spacer(modifier = Modifier.height(10.dp))
            DrawDottedLine(pathEffect = pathEffect)
        }

    }
}

@Composable
fun QuestionTracker(counter: Int = 10, outOf: Int = 100) {
    Text(text = buildAnnotatedString {
        withStyle(
            style = ParagraphStyle(textIndent = TextIndent.None)
        ) {
            withStyle(
                style = SpanStyle(
                    color = AppColors.mLightGray,
                    fontSize = 27.sp,
                    fontWeight = FontWeight.Bold
                )
            ) {
                append("Question ${counter}/")
            }
            withStyle(
                style = SpanStyle(
                    color = AppColors.mLightGray,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Light
                )
            ) {
                append("$outOf")
            }
        }
    })
}

@Composable
fun DrawDottedLine(pathEffect: PathEffect) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(2.dp)
    ) {


        drawLine(
            color = AppColors.mLightGray,
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            pathEffect = pathEffect
        )
    }
}

@Composable
fun DrawQuestionBody(){
    Column {
        Text(text = "What is the meaning of all this ?")
    }
}

