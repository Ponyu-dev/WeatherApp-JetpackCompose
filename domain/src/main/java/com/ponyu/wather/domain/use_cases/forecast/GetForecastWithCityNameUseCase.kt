package com.ponyu.wather.domain.use_cases.forecast

import com.ponyu.wather.domain.interfaces.ForecastRepository
import javax.inject.Inject

class GetForecastWithCityNameUseCase @Inject constructor(
    private val forecastRepository: ForecastRepository
) {
    suspend fun getForecast(cityName: String) =
        forecastRepository.getForecastDataWithCityName(cityName)
}