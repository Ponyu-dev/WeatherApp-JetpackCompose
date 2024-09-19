package com.ponyu.wather.designsystem.component

import androidx.annotation.DrawableRes
import com.ponyu.wather.designsystem.R

private object MainWeatherConditions {
    const val CLOUDS = "Clouds"
    const val SNOW = "Snow"
    const val RAIN = "Rain"
    const val THUNDERSTORM = "Thunderstorm"
    const val CLEAR = "Clear"
}

private object WeatherConditions {
    const val CLEAR_SKY = "clear sky"
    const val FEW_CLOUDS = "few clouds"
    const val SCATTERED_CLOUDS = "scattered clouds"
    const val BROKEN_CLOUDS = "broken clouds"
    const val SHOWER_RAIN = "shower rain"
    const val RAIN = "rain"
    const val THUNDERSTORM = "thunderstorm"
    const val SNOW = "snow"
    const val MIST = "mist"
}

sealed class WeatherType(
    val weatherDescription: String,
    @DrawableRes val id: Int
) {
    data object ClearSkyDay : WeatherType(WeatherConditions.CLEAR_SKY, R.drawable.clear_sky_day)
    data object ClearSkyNight : WeatherType(WeatherConditions.CLEAR_SKY, R.drawable.clear_sky_night)
    data object FewCloudsDay : WeatherType(WeatherConditions.FEW_CLOUDS, R.drawable.few_clouds_day)
    data object FewCloudsNight : WeatherType(WeatherConditions.FEW_CLOUDS, R.drawable.broken_clouds_night)
    data object ScatteredClouds : WeatherType(WeatherConditions.SCATTERED_CLOUDS, R.drawable.scattered_clouds)
    data object BrokenCloudsDay : WeatherType(WeatherConditions.BROKEN_CLOUDS, R.drawable.broken_clouds_day)
    data object BrokenCloudsNight : WeatherType(WeatherConditions.BROKEN_CLOUDS, R.drawable.broken_clouds_night)
    data object ShowerRainDay : WeatherType(WeatherConditions.SHOWER_RAIN, R.drawable.rain_day)
    data object ShowerRainNight : WeatherType(WeatherConditions.SHOWER_RAIN, R.drawable.rain_night)
    data object RainDay : WeatherType(WeatherConditions.RAIN, R.drawable.rain_day)
    data object RainNight : WeatherType(WeatherConditions.RAIN, R.drawable.rain_day)
    data object Thunderstorm : WeatherType(WeatherConditions.THUNDERSTORM, R.drawable.thunderstorrm)
    data object Snow : WeatherType(WeatherConditions.SNOW, R.drawable.snow)
    data object MistDay : WeatherType(WeatherConditions.MIST, R.drawable.mist_day)
    data object MistNight : WeatherType(WeatherConditions.MIST, R.drawable.mist_night)

    companion object {
        private val dayNightMap = mapOf(
            "00" to true, "03" to true, "06" to true,
            "12" to true, "09" to true, "PM" to true // Более логичные сокращения времени
        )

        fun getWeatherType(mainDescription: String, weatherDescription: String, hour: String): Int {
            return when (mainDescription) {
                MainWeatherConditions.CLOUDS -> getCloudImage(weatherDescription, hour)
                MainWeatherConditions.RAIN -> getRainImage(weatherDescription, hour)
                MainWeatherConditions.THUNDERSTORM -> Thunderstorm.id
                MainWeatherConditions.SNOW -> Snow.id
                MainWeatherConditions.CLEAR -> if (checkTime(hour)) ClearSkyNight.id else ClearSkyDay.id
                else -> if (checkTime(hour)) MistNight.id else MistDay.id
            }
        }

        private fun getCloudImage(description: String, hour: String): Int {
            return when (description) {
                ScatteredClouds.weatherDescription -> ScatteredClouds.id
                FewCloudsDay.weatherDescription -> if (checkTime(hour)) FewCloudsNight.id else FewCloudsDay.id
                else -> if (checkTime(hour)) BrokenCloudsNight.id else BrokenCloudsDay.id
            }
        }

        private fun getRainImage(description: String, hour: String): Int {
            return if (description == ShowerRainDay.weatherDescription) {
                if (checkTime(hour)) ShowerRainNight.id else ShowerRainDay.id
            } else {
                if (checkTime(hour)) RainNight.id else RainDay.id
            }
        }

        private fun checkTime(hour: String): Boolean {
            return dayNightMap[hour.substring(0, 2)] == true || hour.contains("PM")
        }
    }
}
