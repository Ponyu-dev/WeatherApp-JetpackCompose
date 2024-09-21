package com.ponyu.wather.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
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
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.tertiary
        ),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 8.dp
        )
    ) {
        WeatherInfo(
            degree,
            latitude, longitude,
            city, country,
            description,
            weatherImage,
            onClick,
            isItDb
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
    weatherImage: Int,
    onClick: () -> Unit = {},
    isItDb: Boolean = false
) {
    Box {
        WeatherImage(weatherImage)
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            DegreeAndButtonSection(degree, isItDb, onClick)
            LocationAndDescription(latitude, longitude, city, country, description)
        }
    }
}

@Composable
private fun WeatherImage(weatherImage: Int) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Image(
            modifier = Modifier.size(LocalConfiguration.current.screenWidthDp.dp / 4),
            painter = painterResource(id = weatherImage),
            contentDescription = ""
        )
    }
}

@Composable
private fun DegreeAndButtonSection(
    degree: String,
    isItDb: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier.padding(16.dp),
            text = degree,
            style = MaterialTheme.typography.displayLarge
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
            Text(
                text = "$city, $country",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "H: $latitude  L: $longitude",
                style = MaterialTheme.typography.bodySmall
            )
        }
        Text(
            modifier = Modifier.padding(end = 16.dp),
            text = description,
            style = MaterialTheme.typography.bodyMedium
        )
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