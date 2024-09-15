package com.ponyu.wather.domain.use_cases.forecast

import com.ponyu.wather.domain.interfaces.ForecastRepository
import com.ponyu.wather.domain.model.Forecast
import javax.inject.Inject

class GetForecastFromDbUseCase @Inject constructor(
    private val forecastRepository: ForecastRepository
) {
    fun getForecastFromDbUseCase() : Forecast? = forecastRepository.getForecastWeather()
}