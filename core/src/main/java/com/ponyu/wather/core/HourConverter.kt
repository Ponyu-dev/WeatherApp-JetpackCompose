package com.ponyu.wather.core

object HourConverter {
    fun convertHour(hour: String): String {
        return when (val hourInt = hour.toInt()) {
            0 -> "12 AM"
            12 -> "12 PM"
            in 1..11 -> "$hour AM"
            else -> "${hourInt - 12}".padStart(2, '0') + " PM"
        }
    }
}