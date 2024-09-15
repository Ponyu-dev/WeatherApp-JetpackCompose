package com.ponyu.wather.domain.use_cases.my_city

import com.ponyu.wather.domain.interfaces.MyCityRepository
import javax.inject.Inject

class DeleteMyCityUseCase @Inject constructor(
    private val myCityRepositoryImpl: MyCityRepository
) {
    fun deleteMyCityUseCase(cityName: String) = myCityRepositoryImpl.deleteMyCity(cityName)
}