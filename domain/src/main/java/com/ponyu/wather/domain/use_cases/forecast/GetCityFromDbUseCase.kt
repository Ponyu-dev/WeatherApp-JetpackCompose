package com.ponyu.wather.domain.use_cases.forecast

import com.ponyu.wather.domain.interfaces.ForecastRepository
import javax.inject.Inject

class GetCityFromDbUseCase @Inject constructor(
    private val forecastRepository: ForecastRepository
) {
    fun getCityFromDb() = forecastRepository.getCity()
}