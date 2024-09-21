package com.ponyu.wather.core

import android.os.Build
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

object Converter {
    fun convertHour(hour: String): String {
        return when (val hourInt = hour.toInt()) {
            0 -> "12 AM"
            12 -> "12 PM"
            in 1..11 -> "$hour AM"
            else -> "${hourInt - 12}".padStart(2, '0') + " PM"
        }
    }

    fun formatDate(dateString: String): Pair<String, String> {
        val dayOfWeek: String
        val formattedDate: String

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Для API 26 и выше используем LocalDateTime
            val dateTime = LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

            // Получение названия дня недели
            dayOfWeek = dateTime.dayOfWeek.getDisplayName(java.time.format.TextStyle.FULL, Locale.getDefault())

            // Форматирование даты
            formattedDate = dateTime.format(DateTimeFormatter.ofPattern("MMMM, d"))
        } else {
            // Для более старых API используем SimpleDateFormat
            val sdfInput = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val sdfOutputDay = SimpleDateFormat("EEEE", Locale.getDefault())
            val sdfOutputDate = SimpleDateFormat("MMMM, d", Locale.getDefault())

            val date = sdfInput.parse(dateString) ?: throw IllegalArgumentException("Invalid date format")

            // Получение названия дня недели
            dayOfWeek = sdfOutputDay.format(date)

            // Форматирование даты
            formattedDate = sdfOutputDate.format(date)
        }

        return Pair(dayOfWeek, formattedDate)
    }
}

fun getCurrentDate(): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        // Для API 26 и выше используем LocalDateTime и DateTimeFormatter
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("EEE, d MMM y", Locale.getDefault())
        current.format(formatter)
    } else {
        // Для API ниже 26 используем Calendar и SimpleDateFormat
        val current = Calendar.getInstance().time
        val formatter = SimpleDateFormat("EEE, d MMM y", Locale.getDefault())
        formatter.format(current)
    }
}