package com.ponyu.wather.domain.interfaces

import android.location.Location

interface LocationTracker {
    suspend fun getCurrentLocation(): Location?
}