package com.ponyu.wather.domain.use_cases.my_city

import com.ponyu.wather.domain.interfaces.MyCityRepository
import com.ponyu.wather.domain.model.MyCity
import javax.inject.Inject

class AddMyCityUseCase @Inject constructor(
    private val myCityRepository: MyCityRepository
) {
    suspend fun addMyCity(myCity: MyCity) = myCityRepository.addMyCity(myCity)
}