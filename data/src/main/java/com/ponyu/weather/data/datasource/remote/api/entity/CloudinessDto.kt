package com.ponyu.weather.data.datasource.remote.api.entity

import com.google.gson.annotations.SerializedName

data class CloudinessDto(
    @SerializedName("all") val cloudiness: Int
)