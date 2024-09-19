package com.ponyu.weather.feature.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ponyu.wather.core.AppStrings
import com.ponyu.wather.domain.model.Forecast
import com.ponyu.wather.designsystem.component.ErrorCard
import com.ponyu.wather.designsystem.component.WeatherType

@Composable
internal fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val homeCurrentWeatherState by viewModel.homeForecastState.collectAsState()

    // Используем LaunchedEffect для вызова loadLocation один раз при первой компоновке
    LaunchedEffect(Unit) {
        viewModel.loadLocation()
    }

    WeatherSection(
        homeCurrentWeatherState
    ) {
        viewModel.loadLocation()
    }
}

@Composable
private fun WeatherSection(
    currentWeatherState: HomeForecastState,
    dialogErrorRetry: () -> Unit,
) {
    when (currentWeatherState) {
        is HomeForecastState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(LocalConfiguration.current.screenWidthDp.dp / 3),
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    strokeWidth = 5.dp
                )
            }
        }
        is HomeForecastState.Success -> {
            if (currentWeatherState.forecast != null) {
                WeatherDataToday(todayWeather = currentWeatherState.forecast)
            }
        }
        is HomeForecastState.Error -> {
            ErrorCard(
                modifier = Modifier.fillMaxSize(),
                errorTitle = currentWeatherState.errorMessage.title,
                errorDescription = currentWeatherState.errorMessage.desc,
                errorButtonText =  currentWeatherState.errorMessage.btnText,
                onClick = {
                    dialogErrorRetry.invoke()
                },
                cardModifier = Modifier
                    .fillMaxWidth()
                    .height(LocalConfiguration.current.screenHeightDp.dp / 4 + 48.dp)
                    .padding(horizontal = 64.dp),
            )
        }
    }
}

@Composable
private fun WeatherDataToday(
    modifier: Modifier = Modifier,
    todayWeather: Forecast
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.primaryContainer)
            .padding(10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, bottom = 10.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Outlined.LocationOn,
                contentDescription = "Location Icon",
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                todayWeather.cityDtoData.cityName,
                style = MaterialTheme.typography.titleMedium
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, end = 15.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                todayWeather.weatherList[0].date,
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(
                    WeatherType.getWeatherType(
                        todayWeather.weatherList[0].weatherStatus[0].mainDescription,
                        todayWeather.weatherList[0].weatherStatus[0].description,
                        "12"
                    )
                ),
                contentDescription = ""
            )
            Text(
                text = todayWeather.weatherList[0].weatherStatus[0].description,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            Text(
                text = "${todayWeather.weatherList[0].weatherData.temp.toInt()}${AppStrings.degree}",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )
        }
    }
}