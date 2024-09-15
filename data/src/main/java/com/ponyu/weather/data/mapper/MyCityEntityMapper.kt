package com.ponyu.weather.data.mapper

import com.ponyu.wather.domain.model.MyCity
import com.ponyu.weather.data.datasource.local.db.entity.MyCityEntity
import javax.inject.Inject

class MyCityEntityMapper @Inject constructor() {
    fun mapFromEntity(entity: List<MyCityEntity>): List<MyCity> {
        return entity.map {
            MyCity(
                it.temp,
                it.latitude,
                it.longitude,
                it.cityName,
                it.country,
                it.description,
                it.weatherImage,
            )
        }
    }

    fun entityFromModel(model: MyCity): MyCityEntity {
        return MyCityEntity(
            temp = model.temp,
            latitude = model.latitude,
            longitude = model.longitude,
            cityName = model.cityName,
            country = model.country,
            description = model.description,
            weatherImage = model.weatherImage
        )
    }
}