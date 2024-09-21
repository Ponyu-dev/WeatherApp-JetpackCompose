package com.ponyu.weather.data.repository

import com.ponyu.wather.core.Constants
import com.ponyu.wather.core.Response
import com.ponyu.wather.domain.interfaces.ForecastRepository
import com.ponyu.wather.domain.model.City
import com.ponyu.wather.domain.model.Forecast
import com.ponyu.weather.data.datasource.local.db.CityLocalDataSource
import com.ponyu.weather.data.datasource.local.db.ForecastLocalDataSource
import com.ponyu.weather.data.datasource.remote.api.ForecastRemoteDataSource
import com.ponyu.weather.data.mapper.CityEntityMapper
import com.ponyu.weather.data.mapper.ForecastDtoMapper
import com.ponyu.weather.data.mapper.ForecastEntityMapper
import javax.inject.Inject

class ForecastRepositoryImpl @Inject constructor(
    private val forecastRemoteDataSource: ForecastRemoteDataSource,
    private val forecastLocalDataSource: ForecastLocalDataSource,
    private val cityLocalDataSource: CityLocalDataSource,
    private val dtoMapper: ForecastDtoMapper,
    private val entityMapper: ForecastEntityMapper,
    private val cityEntityMapper: CityEntityMapper
) : ForecastRepository {
    override suspend fun getForecastData(
        latitude: Double,
        longitude: Double
    ): Response<Forecast> {
        return try {
            Response.Success(
                dtoMapper.mapFromEntity(
                    forecastRemoteDataSource.getForecastData(
                        latitude,
                        longitude
                    )
                )
            )
        } catch (e: Exception) {
            Response.Error(e.message ?: Constants.UNKNOWN_ERROR)
        }
    }

    override suspend fun getForecastDataWithCityName(cityName: String): Response<Forecast> {
        return try {
            Response.Success(
                dtoMapper.mapFromEntity(
                    forecastRemoteDataSource.getForecastDataWithCityName(cityName)
                )
            )
        } catch (e: Exception) {
            Response.Error(e.message ?: Constants.UNKNOWN_ERROR)
        }
    }

    override suspend fun addForecastWeather(forecast: Forecast) {
        forecastLocalDataSource.addForecastWeather(
            entityMapper.entityFromModel(forecast)
        )
    }

    override suspend fun addCity(city: City) {
        forecastLocalDataSource.addCity(
            cityEntityMapper.entityFromModel(city)
        )
    }

    override fun getForecastWeather(): Forecast? {
        return if (forecastLocalDataSource.getForecastWeather().isEmpty()) {
            null
        } else {
            entityMapper.mapFromEntity(
                forecastLocalDataSource.getForecastWeather(),
                cityLocalDataSource.getCity()
            )
        }
    }

    override fun getCity(): City {
        return cityEntityMapper.mapFromEntity(forecastLocalDataSource.getCity())
    }

    override suspend fun updateForecastWeather(forecast: Forecast) {
        forecastLocalDataSource.updateForecastWeather(
            entityMapper.entityFromModel(forecast)
        )
    }

    override suspend fun updateCity(city: City) {
        forecastLocalDataSource.updateCity(
            cityEntityMapper.entityFromModel(city)
        )
    }
}