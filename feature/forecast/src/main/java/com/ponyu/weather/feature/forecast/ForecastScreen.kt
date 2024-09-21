package com.ponyu.weather.feature.forecast

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ponyu.wather.core.Converter
import com.ponyu.wather.core.getCurrentDate
import com.ponyu.wather.designsystem.component.WeatherType
import com.ponyu.wather.domain.interfaces.ForecastState
import com.ponyu.wather.domain.model.ForecastWeather

@Composable
internal fun ForecastScreen(
    modifier: Modifier = Modifier,
    forecastVideModel: ForecastVideModel = hiltViewModel()
) {
    val forecastState by forecastVideModel.forecastState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
    ) {
        ForecastMainElements(
            forecastState,
            forecastVideModel::filterDatesByCurrentDate
        )
    }
}

@Composable
private fun ForecastMainElements(
    forecastState: ForecastState,
    filterFunction: (List<ForecastWeather>, Boolean) -> List<ForecastWeather>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Top()

        when (forecastState) {
            is ForecastState.Loading -> {
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
            is ForecastState.Success -> {
                forecastState.forecast?.weatherList?.let {
                    LazyHourlyCard(filterFunction(it, true))
                    NextForecast()
                    LazyDailyCard(filterFunction(it, false))
                }
            }

            is ForecastState.Error -> { }
        }
    }
}

@Composable
private fun Top() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(com.ponyu.weather.core.R.string.forecast_report),
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Today",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = getCurrentDate(),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun LazyHourlyCard(
    forecasts: List<ForecastWeather>,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(
            vertical = 8.dp
        ),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(forecasts) {
            HourlyCard(
                time = Converter.convertHour(it.date.substring(11, 13)),
                weatherIcon = WeatherType.getWeatherType(
                    it.weatherStatus[0].mainDescription,
                    it.weatherStatus[0].description,
                    Converter.convertHour(it.date.substring(11, 13)),
                ),
                degree = "${it.weatherData.temp.toInt()}"
            )
        }
    }
}

@Composable
private fun HourlyCard(
    time: String,
    weatherIcon: Int,
    degree: String
) {
    Card(
        modifier = Modifier
            .width(180.dp)
            .height(120.dp)
            .padding(8.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            ImageColumn(weatherIcon)
            TemperatureColumn(time, degree)
        }
    }
}

@Composable
private fun ImageColumn(image: Int) {
    Column(
        modifier = Modifier.fillMaxWidth(0.6f),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = image),
            contentDescription = "weather icon"
        )
    }
}

@Composable
private fun TemperatureColumn(
    time: String,
    temperature: String
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = time,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = "$temperature°C",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun NextForecast() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
    ) {
        Box(
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = stringResource(com.ponyu.weather.core.R.string.next_forecast),
                style = MaterialTheme.typography.titleMedium
            )
        }
        Icon(
            imageVector = Icons.Outlined.DateRange,
            contentDescription = "Calendar",
            tint = MaterialTheme.colorScheme.error,//Color(0xFFd68118),
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}

@Composable
private fun LazyDailyCard(
    forecasts: List<ForecastWeather>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(
            vertical = 8.dp
        ),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(forecasts) {
            val (dayOfWeek, formattedDate) = Converter.formatDate(it.date)
            DailyCard(
                day = dayOfWeek,
                date = formattedDate,
                temperature = "${it.weatherData.temp.toInt()}",
                weatherIcon = WeatherType.getWeatherType(
                    it.weatherStatus[0].mainDescription,
                    it.weatherStatus[0].description,
                    Converter.convertHour(it.date.substring(11, 13)),
                )
            )
        }
    }
}

@Composable
fun DailyCard(
    day: String, date: String,
    temperature: String,
    weatherIcon: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(
                horizontal = 16.dp
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(
                modifier = Modifier.padding(start = 10.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = day,
                    style = MaterialTheme.typography.labelLarge
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = date,
                    style = MaterialTheme.typography.labelLarge
                )
            }

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "$temperature°C",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Column(
                modifier = Modifier.padding(4.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = weatherIcon),
                    contentDescription = "weather icon"
                )
            }
        }
    }
}
