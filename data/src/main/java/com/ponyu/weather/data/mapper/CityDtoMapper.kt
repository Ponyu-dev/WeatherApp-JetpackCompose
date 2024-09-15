package com.ponyu.weather.data.mapper

import com.ponyu.wather.domain.interfaces.IEntityMapper
import com.ponyu.wather.domain.model.City
import com.ponyu.wather.domain.model.Coord
import com.ponyu.weather.data.datasource.remote.api.entity.CityDto
import com.ponyu.weather.data.datasource.remote.api.entity.CoordDto
import javax.inject.Inject

class CityDtoMapper @Inject constructor() : IEntityMapper<CityDto, City> {
    override fun mapFromEntity(entity: CityDto): City {
        return City(
            entity.country,
            entity.timezone,
            entity.sunrise,
            entity.sunset,
            entity.cityName,
            Coord(
                entity.coordinate.latitude,
                entity.coordinate.longitude
            )
        )
    }

    override fun entityFromModel(model: City): CityDto {
        return CityDto(
            model.country,
            model.timezone,
            model.sunrise,
            model.sunset,
            model.cityName,
            CoordDto(
                model.coordinate.latitude,
                model.coordinate.longitude
            )
        )
    }

}