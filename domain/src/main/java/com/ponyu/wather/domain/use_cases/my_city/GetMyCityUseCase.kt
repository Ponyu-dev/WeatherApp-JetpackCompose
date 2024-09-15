package com.ponyu.wather.domain.use_cases.my_city

import com.ponyu.wather.domain.interfaces.MyCityRepository
import javax.inject.Inject

class GetMyCityUseCase @Inject constructor(
    private val myCityRepository: MyCityRepository
) {
    fun getMyCity() = myCityRepository.getMyCity()
}