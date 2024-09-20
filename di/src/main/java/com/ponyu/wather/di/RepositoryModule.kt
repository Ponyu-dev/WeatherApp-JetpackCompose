package com.ponyu.wather.di

import com.ponyu.wather.domain.interfaces.ForecastRepository
import com.ponyu.wather.domain.interfaces.MyCityRepository
import com.ponyu.weather.data.repository.ForecastRepositoryImpl
import com.ponyu.weather.data.repository.MyCityRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindForecastRepository(forecastRepositoryImpl: ForecastRepositoryImpl): ForecastRepository

    @Binds
    @Singleton
    abstract fun bindMyCityRepository(myCityRepositoryImpl: MyCityRepositoryImpl): MyCityRepository
}