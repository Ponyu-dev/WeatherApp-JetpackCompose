package com.ponyu.wather.domain.use_cases.my_city

import com.ponyu.wather.domain.interfaces.MyCityRepository
import javax.inject.Inject

class GetSpecificCityUseCase @Inject constructor(
    private val myCityRepository: MyCityRepository
) {
    suspend fun getSpecificCityUseCase(cityName: String) =
        myCityRepository.getSpecificCity(cityName)
}