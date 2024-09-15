package com.ponyu.wather.domain.interfaces

import com.ponyu.wather.core.Response
import com.ponyu.wather.domain.model.City
import com.ponyu.wather.domain.model.Forecast

interface ForecastRepository {
    suspend fun getForecastData(latitude: Double, longitude: Double, ): Response<Forecast>

    suspend fun getForecastDataWithCityName(cityName: String): Response<Forecast>

    suspend fun addForecastWeather(forecast: Forecast)

    suspend fun addCity(city: City)

    fun getForecastWeather() : Forecast?

    fun getCity() : City

    suspend fun updateForecastWeather(forecast: Forecast)

    suspend fun updateCity(city: City)
}