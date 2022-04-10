package com.android.jetweatherforecast.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.android.jetweatherforecast.R
import com.android.jetweatherforecast.model.WeatherItem
import com.android.jetweatherforecast.utils.formatDate
import com.android.jetweatherforecast.utils.formatDateTime
import com.android.jetweatherforecast.utils.formatDecimals


@Composable
fun HumidityWindPressureRow(weather: WeatherItem, isImperial: Boolean) {
    Row(
        modifier = Modifier
            .padding(15.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                modifier = Modifier.size(25.dp),
                painter = painterResource(id = R.drawable.pressure),
                contentDescription = "Pressure Icon"
            )
            Text(
                text = "${weather.pressure} psi",
                style = MaterialTheme.typography.caption
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                modifier = Modifier.size(25.dp),
                painter = painterResource(id = R.drawable.wind),
                contentDescription = "Wind Icon"
            )
            Text(
                text = "${formatDecimals(weather.speed)} " +if (isImperial)"mph" else "m/s",
                style = MaterialTheme.typography.caption
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                modifier = Modifier.size(25.dp),
                painter = painterResource(id = R.drawable.humidity),
                contentDescription = "Humidity Icon"
            )
            Text(
                text = "${weather.humidity} %",
                style = MaterialTheme.typography.caption
            )
        }
    }
}

@Composable
fun SunSetSunRiseRow(weather: WeatherItem) {
    Row(
        modifier = Modifier
            .padding(15.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                modifier = Modifier.size(25.dp),
                painter = painterResource(id = R.drawable.sunrise),
                contentDescription = "Sunrise Icon"
            )
            Text(
                text = formatDateTime(weather.sunrise),
                style = MaterialTheme.typography.caption
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                modifier = Modifier.size(25.dp),
                painter = painterResource(id = R.drawable.sunset),
                contentDescription = "Sunset Icon"
            )
            Text(
                text = formatDateTime(weather.sunset),
                style = MaterialTheme.typography.caption
            )
        }
    }
}


@Composable
fun WeeklyDetailRow(weatherItem: WeatherItem) {
    val imageUrl = "https://openweathermap.org/img/wn/${weatherItem.weather[0].icon}.png"
    Surface(
        modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth(),

        shape = RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp, bottomEnd = 20.dp),
        color = Color.White

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(formatDate(weatherItem.dt).split(",")[0])
            Image(
                modifier = Modifier.size(60.dp),
                painter = rememberImagePainter(imageUrl),
                contentDescription = ""
            )
            Surface(
                shape = RoundedCornerShape(percent = 50),
                color = Color(0xFFFDBA29)
            ) {
                Text(
                    modifier = Modifier.padding(4.dp),
                    text = weatherItem.weather[0].description
                )
            }


            Text(text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = Color.Red.copy(alpha = 0.7f),
                        fontWeight = FontWeight.SemiBold
                    )
                ) {
                    append(formatDecimals(weatherItem.temp.max) + "°")
                }
                withStyle(
                    style = SpanStyle(
                        color = Color.LightGray,
                        fontWeight = FontWeight.SemiBold
                    )
                ) {
                    append(formatDecimals(weatherItem.temp.min) + "°")
                }
            })


        }
    }

}

@Composable
fun WeatherStateImage(imageUrl: String) {
    Image(
        modifier = Modifier.size(80.dp),
        painter = rememberImagePainter(imageUrl),
        contentDescription = "icon image"
    )
}