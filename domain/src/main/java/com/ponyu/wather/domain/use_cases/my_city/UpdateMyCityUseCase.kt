package com.ponyu.wather.domain.use_cases.my_city

import com.ponyu.wather.domain.interfaces.MyCityRepository
import com.ponyu.wather.domain.model.MyCity
import javax.inject.Inject

class UpdateMyCityUseCase @Inject constructor(
    private val myCityRepository: MyCityRepository
) {
    suspend fun updateMyCityUseCase(myCity: MyCity) = myCityRepository.updateMyCity(myCity)
}