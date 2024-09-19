package com.ponyu.weather.feature.home

import com.ponyu.wather.domain.model.Forecast
import com.ponyu.wather.domain.use_cases.string_exctenstions.ExceptionMessage

sealed interface HomeForecastState {
    data class Success(val forecast: Forecast?): HomeForecastState
    data class Error(val errorMessage: ExceptionMessage): HomeForecastState

    object Loading: HomeForecastState
}