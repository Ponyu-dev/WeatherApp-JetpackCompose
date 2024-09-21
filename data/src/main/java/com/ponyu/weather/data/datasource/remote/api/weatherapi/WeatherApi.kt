package com.ponyu.weather.data.datasource.remote.api.weatherapi

import com.ponyu.wather.core.NetworkService
import com.ponyu.weather.data.BuildConfig
import com.ponyu.weather.data.datasource.remote.api.entity.ForecastDto
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET(NetworkService.FORECAST_END_POINT)
    suspend fun getForecastData(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("APPID") apiKey: String = BuildConfig.API_KEY,
        @Query("units") units: String = NetworkService.UNITS,
    ): ForecastDto

    @GET(NetworkService.FORECAST_END_POINT)
    suspend fun getForecastDataWithCityName(
        @Query("q") cityName: String,
        @Query("APPID") apiKey: String = BuildConfig.API_KEY,
        @Query("units") units: String = NetworkService.UNITS,
    ): ForecastDto
}