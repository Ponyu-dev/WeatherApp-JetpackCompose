package com.ponyu.wather.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ponyu.wather.designsystem.R

@Composable
fun CityWeatherCard(
    modifier: Modifier = Modifier,
    degree: String,
    latitude: Double,
    longitude: Double,
    city: String,
    country: String,
    description: String,
    weatherImage: Int,
    onClick: () -> Unit = {},
    isItDb: Boolean = false
) {
    Card(
        modifier = modifier,
        backgroundColor = MaterialTheme.colors.background,
        shape = MaterialTheme.shapes.medium
    ) {
        WeatherImage(weatherImage)
        WeatherInfo(degree, latitude, longitude, city, country, description, onClick, isItDb)
    }
}

@Composable
private fun WeatherImage(weatherImage: Int) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterEnd) {
        Image(
            modifier = Modifier.size(LocalConfiguration.current.screenWidthDp.dp / 2),
            painter = painterResource(id = weatherImage),
            contentDescription = null
        )
    }
}

@Composable
private fun WeatherInfo(
    degree: String,
    latitude: Double,
    longitude: Double,
    city: String,
    country: String,
    description: String,
    onClick: () -> Unit = {},
    isItDb: Boolean = false
) {
    Column(modifier = Modifier.fillMaxSize()) {
        DegreeAndButtonSection(degree, isItDb, onClick)
        LocationAndDescription(latitude, longitude, city, country, description)
    }
}

@Composable
private fun DegreeAndButtonSection(degree: String, isItDb: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = degree,
            fontSize = 76.sp
        )
        ActionButton(
            onClick = onClick,
            painterId = if (isItDb) R.drawable.ic_baseline_close else R.drawable.ic_baseline_add
        )
    }
}

@Composable
private fun LocationAndDescription(
    latitude: Double,
    longitude: Double,
    city: String,
    country: String,
    description: String,
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 16.dp, top = 40.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.padding(start = 16.dp)) {
            Text(text = "H: $latitude  L: $longitude")
            Text(text = "$city, $country")
        }
        Text(modifier = Modifier.padding(end = 16.dp), text = description)
    }
}

@Composable
private fun ActionButton(
    onClick: () -> Unit,
    painterId: Int
) {
    IconButton(onClick = onClick) {
        Icon(
            painter = painterResource(id = painterId),
            contentDescription = null,
            tint = Color.White
        )
    }
}