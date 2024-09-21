package com.ponyu.wather.domain.interfaces

import com.ponyu.wather.domain.model.Forecast
import com.ponyu.wather.domain.exctenstions.ExceptionMessage

sealed interface ForecastState {
    data class Success(val forecast: Forecast?): ForecastState
    data class Error(val errorMessage: ExceptionMessage): ForecastState

    object Loading: ForecastState
}