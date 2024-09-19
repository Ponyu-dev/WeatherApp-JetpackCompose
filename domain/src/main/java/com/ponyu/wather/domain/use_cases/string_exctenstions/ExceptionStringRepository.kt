package com.ponyu.wather.domain.use_cases.string_exctenstions

import android.app.Application
import com.ponyu.weather.core.R
import javax.inject.Inject

data class ExceptionMessage(
    val title: String,
    val desc: String,
    val btnText: String
)

class ExceptionStringRepository @Inject constructor(
    private val context: Application
) {
    fun titleNoInternetConnectionMessage(): String {
        return context.getString(R.string.title_no_internet_connection)
    }

    fun descNoInternetConnectionMessage(): String {
        return context.getString(R.string.desc_no_internet_connection)
    }

    fun titleUnknownErrorMessage(): String {
        return context.getString(R.string.title_unknown_error)
    }

    fun descUnknownErrorMessage(): String {
        return context.getString(R.string.desc_unknown_error)
    }

    fun titleNoPermission(): String {
        return context.getString(R.string.title_no_permission)
    }

    fun descNoPermission(): String {
        return context.getString(R.string.desc_no_permission)
    }

    fun titleGpsDisabled(): String {
        return context.getString(R.string.title_gps_disabled)
    }

    fun descGpsDisabled(): String {
        return context.getString(R.string.desc_gps_disabled)
    }

    fun exceptionMessage(errorTitle: String?): ExceptionMessage {
        return when (errorTitle) {
            titleGpsDisabled() -> ExceptionMessage(titleGpsDisabled(), descGpsDisabled(), context.getString(R.string.ok))
            titleNoInternetConnectionMessage() -> ExceptionMessage(titleNoInternetConnectionMessage(), descNoInternetConnectionMessage(), context.getString(R.string.ok))
            titleNoPermission() -> ExceptionMessage(titleNoPermission(), descNoPermission(), context.getString(R.string.ok))
            else -> ExceptionMessage(titleUnknownErrorMessage(), descUnknownErrorMessage(), context.getString(R.string.ok))
        }
    }
}
