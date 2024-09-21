package com.ponyu.weather.feature.forecast

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.ponyu.wather.core.getCurrentDate
import com.ponyu.wather.domain.exctenstions.ExceptionStringRepository
import com.ponyu.wather.domain.interfaces.ForecastState
import com.ponyu.wather.domain.model.ForecastWeather
import com.ponyu.wather.domain.use_cases.forecast.GetForecastFromDbUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ForecastVideModel @Inject constructor(
    private val getForecastDb: GetForecastFromDbUseCase,
    private val exceptionStringRepository: ExceptionStringRepository
) : ViewModel() {

    private val _forecastState = MutableStateFlow<ForecastState>(ForecastState.Loading)
    val forecastState = _forecastState.asStateFlow()

    init {
        loadLocation()
    }

    private fun loadLocation() {
        _forecastState.value = ForecastState.Loading
        if (isForecastCached()) {
            getCachedForecast()
        } else {
            _forecastState.value = ForecastState.Error(exceptionStringRepository.exceptionMessage("Is Empty"))
        }
    }

    private fun getCachedForecast() {
        _forecastState.value = ForecastState.Success(getForecastDb.getForecastFromDbUseCase()!!)
    }

    private fun isForecastCached(): Boolean {
        return getForecastDb.getForecastFromDbUseCase() != null
    }

    //TODO убрать сравнение через строки. И делать сравнение через даты.
    @SuppressLint("DefaultLocale")
    fun filterDatesByCurrentDate(
        forecastWeather: List<ForecastWeather>,
        matchCurrentDate: Boolean
    ): List<ForecastWeather> {
        val currentDate = getCurrentDate()
        val uniqueDates = mutableSetOf<String>()

        return forecastWeather.filter { weather ->
            val dateWithoutTime = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // Для API 26 и выше используем LocalDateTime для парсинга
                val dateTime = LocalDateTime.parse(weather.date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                dateTime.toLocalDate().format(DateTimeFormatter.ofPattern("EEE, d MMM y"))
            } else {
                // Для более старых API используем SimpleDateFormat
                val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(weather.date)
                date?.let {
                    val calendar = Calendar.getInstance().apply { time = it }
                    // Используем дату из календаря, берем "середину дня" (например, полдень)
                    String.format("%s, %d %s",
                        SimpleDateFormat("EEE", Locale.getDefault()).format(it),
                        calendar.get(Calendar.DAY_OF_MONTH),
                        SimpleDateFormat("MMM", Locale.getDefault()).format(it)
                    )
                }
            }

            // Если matchCurrentDate == true, фильтруем совпадающие с текущей датой
            // Если matchCurrentDate == false, фильтруем те, что НЕ совпадают и добавляем уникальные даты
            if (matchCurrentDate) {
                dateWithoutTime == currentDate
            } else {
                // Добавляем уникальные даты
                if (dateWithoutTime != null && uniqueDates.add(dateWithoutTime)) {
                    true
                } else {
                    false
                }
            }
        }
    }

    /*fun filterDatesByCurrentDate(
        forecastWeather: List<ForecastWeather>,
        matchCurrentDate: Boolean
    ): List<ForecastWeather> {
        val currentDate = getCurrentDate()

        val result = forecastWeather.filter { it ->
            val dateWithoutTime = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // Для API 26 и выше используем LocalDateTime для парсинга
                val dateTime = LocalDateTime.parse(it.date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                dateTime.format(DateTimeFormatter.ofPattern("EEE, d MMM y"))
            } else {
                // Для более старых API используем SimpleDateFormat
                val dateTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(it.date)
                dateTime?.let { SimpleDateFormat("EEE, d MMM y", Locale.getDefault()).format(it) }
            }

            if (matchCurrentDate) {
                dateWithoutTime == currentDate
            } else {
                dateWithoutTime != currentDate
            }
        }

        return result
    }*/
}