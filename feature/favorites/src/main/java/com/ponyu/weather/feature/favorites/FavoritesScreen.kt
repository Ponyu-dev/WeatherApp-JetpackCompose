package com.ponyu.weather.feature.favorites

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalGraphicsContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ponyu.wather.core.HourConverter
import com.ponyu.wather.designsystem.component.CityWeatherCard
import com.ponyu.wather.designsystem.component.ErrorCard
import com.ponyu.wather.designsystem.component.WeatherType
import com.ponyu.wather.domain.model.Forecast
import com.ponyu.wather.domain.model.MyCity

@Composable
internal fun FavoritesScreen(
    modifier: Modifier = Modifier,
    favoritesViewModel: FavoritesViewModel = hiltViewModel()
) {
    val searchCityState by favoritesViewModel.searchCityState.collectAsState()
    val myCitiesState by favoritesViewModel.myCitiesState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.primaryContainer)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SearchField(
            searchFieldValue = favoritesViewModel.searchFieldValue,
            onValueChange = favoritesViewModel::updateSearchField,
            onSearchCityClick = favoritesViewModel::searchCityClick
        )

        if (favoritesViewModel.isCitySearched) {
            when (searchCityState) {
                is SearchCityState.Loading -> LoadingIndicator()
                is SearchCityState.Success -> (searchCityState as SearchCityState.Success).forecast?.let {
                    WantedCityWeatherSection(it, favoritesViewModel)
                }

                is SearchCityState.Error -> ErrorDisplay((searchCityState as SearchCityState.Error).errorMessage.desc) {
                    favoritesViewModel.errorOnClick()
                }
            }
            MyCities(myCitiesState, favoritesViewModel)
        }
        else {
            MyCities(myCitiesState, favoritesViewModel)
        }
    }
}

@Composable
private fun SearchField(
    searchFieldValue: String,
    onValueChange: (String) -> Unit,
    onSearchCityClick: () -> Unit
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = searchFieldValue,
        onValueChange = onValueChange,
        label = { Text(text = stringResource(com.ponyu.weather.core.R.string.title_search_city)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        maxLines = 1,
        trailingIcon = {
            IconButton(onClick = onSearchCityClick) {
                Icon(
                    painter = painterResource(id = com.ponyu.wather.designsystem.R.drawable.ic_baseline_search_24),
                    contentDescription = null
                )
            }
        }
    )
}

@Composable
private fun LoadingIndicator() {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(modifier = Modifier.size(LocalConfiguration.current.screenWidthDp.dp / 3))
    }
}

@Composable
private fun ErrorDisplay(errorMessage: String, onRetry: () -> Unit) {
    ErrorCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        errorTitle = stringResource(com.ponyu.weather.core.R.string.error_title),
        errorDescription = errorMessage,
        errorButtonText = stringResource(com.ponyu.weather.core.R.string.ok),
        onClick = onRetry,
        cardModifier = Modifier
            .fillMaxWidth()
            .height(LocalConfiguration.current.screenHeightDp.dp / 4 + 48.dp)
    )
}

@Composable
private fun WantedCityWeatherSection(
    forecast: Forecast,
    viewModel: FavoritesViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        Text(
            text = stringResource(com.ponyu.weather.core.R.string.subtitle2),
            style = MaterialTheme.typography.titleMedium
        )
        CityWeatherCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(LocalConfiguration.current.screenHeightDp.dp / 4)
                .padding(top = 16.dp),
            degree = stringResource(com.ponyu.weather.core.R.string.degree, forecast.weatherList[0].weatherData.temp.toInt()),
            latitude = forecast.cityDtoData.coordinate.latitude,
            longitude = forecast.cityDtoData.coordinate.longitude,
            city = forecast.cityDtoData.cityName,
            country = forecast.cityDtoData.country,
            description = forecast.weatherList[0].weatherStatus[0].description,
            weatherImage = WeatherType.getWeatherType(
                forecast.weatherList[0].weatherStatus[0].mainDescription,
                forecast.weatherList[0].weatherStatus[0].description,
                HourConverter.convertHour(forecast.weatherList[0].date.substring(11, 13)),
            ),
            isItDb = false,
            onClick = {
                viewModel.addMyCity(
                    MyCity(
                        temp = forecast.weatherList[0].weatherData.temp,
                        latitude = forecast.cityDtoData.coordinate.latitude,
                        longitude = forecast.cityDtoData.coordinate.longitude,
                        cityName = forecast.cityDtoData.cityName,
                        country = forecast.cityDtoData.country,
                        description = forecast.weatherList[0].weatherStatus[0].description,
                        weatherImage = WeatherType.getWeatherType(
                            forecast.weatherList[0].weatherStatus[0].mainDescription,
                            forecast.weatherList[0].weatherStatus[0].description,
                            HourConverter.convertHour(forecast.weatherList[0].date.substring(11, 13)),
                    )
                ))
            }
        )
    }
}

@Composable
private fun MyCities(
    myCitiesState: MyCitiesState,
    viewModel: FavoritesViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.Start
    ) {
        when (myCitiesState) {
            is MyCitiesState.Loading -> LoadingIndicator()
            is MyCitiesState.Success -> {
                if (myCitiesState.forecast.isNullOrEmpty()) {
                    EmptyCityListMessage()
                } else {
                    CityListSection(myCitiesState.forecast, viewModel)
                }
            }
            is MyCitiesState.Error -> ErrorDisplay(myCitiesState.errorMessage.desc) {
                // Add appropriate retry action here if needed
            }
        }
    }
}

@Composable
private fun CityListSection(
    cityList: List<MyCity>,
    viewModel: FavoritesViewModel
) {
    Text(
        text = stringResource(com.ponyu.weather.core.R.string.subtitle1),
        style = MaterialTheme.typography.titleMedium
    )
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Red),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        items(cityList) { city ->
            CityWeatherCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(LocalConfiguration.current.screenHeightDp.dp / 4)
                    .padding(top = 16.dp),
                degree = stringResource(com.ponyu.weather.core.R.string.degree,city.temp.toInt()),
                latitude = city.latitude,
                longitude = city.longitude,
                city = city.cityName,
                country = city.country,
                description = city.description,
                weatherImage = city.weatherImage,
                isItDb = true,
                onClick = { viewModel.removeMyCity(city.cityName) }
            )
        }
    }
}

@Composable
private fun EmptyCityListMessage() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Image(
            modifier = Modifier
                .size(128.dp)
                .padding(bottom = 16.dp),
            painter = painterResource(id = com.ponyu.wather.designsystem.R.drawable.no_city),
            contentDescription = null
        )
        Text(text = stringResource(com.ponyu.weather.core.R.string.no_city))
    }
}
