package com.ponyu.weather.feature.favorites

import com.ponyu.wather.domain.model.Forecast
import com.ponyu.wather.domain.exctenstions.ExceptionMessage

sealed interface SearchCityState {
    data class Success(val forecast: Forecast?): SearchCityState
    data class Error(val errorMessage: ExceptionMessage): SearchCityState

    object Loading: SearchCityState
}