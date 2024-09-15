package com.ponyu.wather.domain.use_cases.location

import com.ponyu.wather.domain.interfaces.LocationTracker
import javax.inject.Inject

class GetLocationUseCase @Inject constructor(
    private val defaultLocationTracker: LocationTracker
) {
    suspend fun getLocation() = defaultLocationTracker.getCurrentLocation()
}