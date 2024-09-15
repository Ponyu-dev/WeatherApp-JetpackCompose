package com.ponyu.wather.domain.use_cases.forecast

import com.ponyu.wather.core.Response
import com.ponyu.wather.domain.interfaces.ForecastRepository
import com.ponyu.wather.domain.model.Forecast
import javax.inject.Inject

class GetForecastUseCase @Inject constructor(
    private val forecastRepository: ForecastRepository
) {
    suspend fun getForecast(latitude: Double, longitude: Double): Response<Forecast> =
        forecastRepository.getForecastData(latitude, longitude)
}