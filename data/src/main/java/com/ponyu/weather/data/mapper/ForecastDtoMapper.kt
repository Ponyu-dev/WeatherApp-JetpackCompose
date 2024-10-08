package com.ponyu.weather.data.mapper

import com.ponyu.wather.domain.interfaces.IEntityMapper
import com.ponyu.wather.domain.model.City
import com.ponyu.wather.domain.model.Cloudiness
import com.ponyu.wather.domain.model.Coord
import com.ponyu.wather.domain.model.Forecast
import com.ponyu.wather.domain.model.ForecastWeather
import com.ponyu.wather.domain.model.Main
import com.ponyu.wather.domain.model.Weather
import com.ponyu.wather.domain.model.Wind
import com.ponyu.weather.data.datasource.remote.api.entity.CityDto
import com.ponyu.weather.data.datasource.remote.api.entity.CloudinessDto
import com.ponyu.weather.data.datasource.remote.api.entity.CoordDto
import com.ponyu.weather.data.datasource.remote.api.entity.ForecastDto
import com.ponyu.weather.data.datasource.remote.api.entity.ForecastWeatherDto
import com.ponyu.weather.data.datasource.remote.api.entity.MainDto
import com.ponyu.weather.data.datasource.remote.api.entity.WeatherDto
import com.ponyu.weather.data.datasource.remote.api.entity.WindDto
import javax.inject.Inject

class ForecastDtoMapper @Inject constructor() : IEntityMapper<ForecastDto, Forecast> {
    override fun mapFromEntity(entity: ForecastDto): Forecast {
        val forecastWeather: List<ForecastWeather> = entity.weatherList.map {
            ForecastWeather(
                weatherData = Main(
                    it.weatherData.temp,
                    it.weatherData.feelsLike,
                    it.weatherData.pressure,
                    it.weatherData.humidity
                ),
                weatherStatus = listOf(
                    Weather(it.weatherStatus[0].mainDescription, it.weatherStatus[0].description)
                ),
                wind = Wind(it.wind.speed),
                date = it.date,
                cloudiness = Cloudiness(it.cloudinessDto.cloudiness)
            )
        }

        return Forecast(
            forecastWeather,
            City(
                entity.cityDtoData.country,
                entity.cityDtoData.timezone,
                entity.cityDtoData.sunrise,
                entity.cityDtoData.sunset,
                entity.cityDtoData.cityName,
                Coord(
                    entity.cityDtoData.coordinate.latitude,
                    entity.cityDtoData.coordinate.longitude
                )
            )
        )
    }

    override fun entityFromModel(model: Forecast): ForecastDto {
        val forecastWeatherDto: List<ForecastWeatherDto> = model.weatherList.map {
            ForecastWeatherDto(
                MainDto(
                    it.weatherData.temp,
                    it.weatherData.feelsLike,
                    it.weatherData.pressure,
                    it.weatherData.humidity
                ),
                listOf(
                    WeatherDto(it.weatherStatus[0].mainDescription, it.weatherStatus[0].description)
                ),
                WindDto(it.wind.speed),
                it.date,
                CloudinessDto(it.cloudiness.cloudiness)
            )
        }

        return ForecastDto(
            forecastWeatherDto,
            CityDto(
                model.cityDtoData.country,
                model.cityDtoData.timezone,
                model.cityDtoData.sunrise,
                model.cityDtoData.sunset,
                model.cityDtoData.cityName,
                CoordDto(
                    model.cityDtoData.coordinate.latitude,
                    model.cityDtoData.coordinate.longitude
                )
            )
        )
    }
}