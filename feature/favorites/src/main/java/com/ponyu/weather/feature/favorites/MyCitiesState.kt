package com.ponyu.weather.feature.favorites

import com.ponyu.wather.domain.model.MyCity
import com.ponyu.wather.domain.exctenstions.ExceptionMessage

sealed interface MyCitiesState {
    data class Success(val forecast: List<MyCity>?): MyCitiesState
    data class Error(val errorMessage: ExceptionMessage): MyCitiesState

    object Loading: MyCitiesState
}