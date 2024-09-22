package com.ponyu.wather.core

//TODO Переместить все эти константы по нужным местам.
// Текст в текстовые ресурсы.
object NetworkService {
    const val BASE_URL: String = "https://api.openweathermap.org"
    const val UNITS: String = "metric"
    const val FORECAST_END_POINT = "/data/2.5/forecast"
}

object Database {
    const val forecast_table = "forecast_data"
    const val database_name = "weather_data.db"
    const val city_table = "city_data"
    const val my_city_table = "my_city"
}

object Constants {
    const val UNKNOWN_ERROR = "An unknown error occurred."
    const val FILL_FIELD = "Please fill in the field."
    const val DATA_NOT_FOUND = "No data available."
    const val UNKNOWN_HOST = "Unable to resolve host \"api.openweathermap.org\": No address associated with hostname"
}