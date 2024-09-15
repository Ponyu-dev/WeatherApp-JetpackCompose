package com.ponyu.wather.domain.use_cases.forecast

import com.ponyu.wather.domain.interfaces.ForecastRepository
import com.ponyu.wather.domain.model.City
import javax.inject.Inject

class UpdateCityDbUseCase @Inject constructor(
    private val forecastRepository: ForecastRepository
) {
    suspend fun updateCityDb(city: City) = forecastRepository.updateCity(city)
}