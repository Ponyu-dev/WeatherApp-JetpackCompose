package com.ponyu.weather.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ponyu.wather.core.Response
import com.ponyu.wather.domain.model.City
import com.ponyu.wather.domain.model.Forecast
import com.ponyu.wather.domain.use_cases.forecast.*
import com.ponyu.wather.domain.use_cases.location.GetLocationUseCase
import com.ponyu.wather.domain.exctenstions.ExceptionStringRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val addForecastDb: AddForecastToDbUseCase,
    private val addCityDb: AddCityToDbUseCase,
    private val updateCityDbUseCase: UpdateCityDbUseCase,
    private val getForecastDb: GetForecastFromDbUseCase,
    private val updateForecastDb: UpdateForecastDbUseCase,
    private val getForecast: GetForecastUseCase,
    private val getCurrentLocation: GetLocationUseCase,
    private val exceptionStringRepository: ExceptionStringRepository
) : ViewModel() {

    private val _homeForecastState = MutableStateFlow<HomeForecastState>(HomeForecastState.Loading)
    val homeForecastState = _homeForecastState.asStateFlow()

    init {
        loadLocation()
    }

    fun loadLocation() {
        _homeForecastState.value = HomeForecastState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val locationData = getCurrentLocation.getLocation()
                if (locationData != null) {
                    fetchAndCacheForecast(locationData.latitude, locationData.longitude)
                } else {
                    handleCachedOrError(exceptionStringRepository.titleNoInternetConnectionMessage())
                }
            } catch (e: Exception) {
                handleCachedOrError(e.message)
            }
        }
    }

    private suspend fun fetchAndCacheForecast(latitude: Double, longitude: Double) {
        when (val result = getForecast.getForecast(latitude, longitude)) {
            is Response.Success -> {
                result.data?.let { forecast ->
                    _homeForecastState.value = HomeForecastState.Success(forecast)
                    cacheOrUpdateForecast(forecast)
                }
            }
            is Response.Error -> {
                _homeForecastState.value = HomeForecastState.Error(exceptionStringRepository.exceptionMessage(result.message))
            }
        }
    }

    private suspend fun cacheOrUpdateForecast(forecast: Forecast) {
        withContext(Dispatchers.IO) {
            val city = forecast.cityDtoData
            if (isForecastCached()) {
                updateCachedForecast(forecast, city)
            } else {
                cacheForecast(forecast, city)
            }
        }
    }

    private suspend fun cacheForecast(forecast: Forecast, city: City) {
        addForecastDb.addForecastToDbUseCase(forecast, forecast.weatherList.size)
        addCityDb.addCityDb(city)
    }

    private suspend fun updateCachedForecast(forecast: Forecast, city: City) {
        updateForecastDb.updateForecastDbUseCase(forecast, forecast.weatherList.size)
        updateCityDbUseCase.updateCityDb(city)
    }

    private fun getCachedForecast() {
        _homeForecastState.value = HomeForecastState.Success(getForecastDb.getForecastFromDbUseCase()!!)
    }

    private fun isForecastCached(): Boolean {
        return getForecastDb.getForecastFromDbUseCase() != null
    }

    private fun handleCachedOrError(errorMessage: String?) {
        if (isForecastCached()) {
            getCachedForecast()
        } else {
            _homeForecastState.value = HomeForecastState.Error(exceptionStringRepository.exceptionMessage(errorMessage))
        }
    }
}