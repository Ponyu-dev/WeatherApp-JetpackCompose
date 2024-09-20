package com.ponyu.weather.feature.favorites

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ponyu.wather.core.Constants
import com.ponyu.wather.core.HourConverter
import com.ponyu.wather.core.Response
import com.ponyu.wather.designsystem.component.WeatherType
import com.ponyu.wather.domain.model.MyCity
import com.ponyu.wather.domain.use_cases.forecast.GetForecastWithCityNameUseCase
import com.ponyu.wather.domain.use_cases.my_city.AddMyCityUseCase
import com.ponyu.wather.domain.use_cases.my_city.DeleteMyCityUseCase
import com.ponyu.wather.domain.use_cases.my_city.GetMyCityUseCase
import com.ponyu.wather.domain.use_cases.my_city.GetSpecificCityUseCase
import com.ponyu.wather.domain.use_cases.my_city.UpdateMyCityUseCase
import com.ponyu.wather.domain.exctenstions.ExceptionStringRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val getForecastWithCityName: GetForecastWithCityNameUseCase,
    private val getMyCityUseCase: GetMyCityUseCase,
    private val addMyCityUseCase: AddMyCityUseCase,
    private val deleteMyCityUseCase: DeleteMyCityUseCase,
    private val updateMyCityUseCase: UpdateMyCityUseCase,
    private val getSpecificCityUseCase: GetSpecificCityUseCase,
    private val exceptionStringRepository: ExceptionStringRepository
) : ViewModel() {

    private val _searchCityState = MutableStateFlow<SearchCityState>(SearchCityState.Loading)
    val searchCityState = _searchCityState.asStateFlow()

    private val _myCitiesState = MutableStateFlow<MyCitiesState>(MyCitiesState.Loading)
    val myCitiesState = _myCitiesState.asStateFlow()

    var searchFieldValue by mutableStateOf("")
        private set

    var isCitySearched by mutableStateOf(false)
        private set

    init {
        loadMyCities()
    }

    fun errorOnClick() {
        _searchCityState.value = SearchCityState.Success(null)
    }

    fun searchCityClick() {
        isCitySearched = true
        Log.e("FavoritesViewModel", "searchCityClick")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.e("FavoritesViewModel", "${checkSearchFieldValue()}")
                if (checkSearchFieldValue()) {
                    fetchForecastWithCityName(searchFieldValue)
                } else {
                    _searchCityState.value = SearchCityState.Error(exceptionStringRepository.exceptionMessage(Constants.FILL_FIELD))
                }
            } catch (e: Exception) {
                _searchCityState.value = SearchCityState.Error(exceptionStringRepository.exceptionMessage(e.message))
            }
        }
    }

    private fun loadMyCities() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (isMyCitiesExist()) {
                    updateMyCity()
                } else {
                    _myCitiesState.value = MyCitiesState.Success(emptyList())
                }
            } catch (e: Exception) {
                _myCitiesState.value = MyCitiesState.Error(exceptionStringRepository.exceptionMessage(e.message))
            }
        }
    }

    private suspend fun fetchForecastWithCityName(cityName: String) {
        Log.e("FavoritesViewModel", "fetchForecastWithCityName $searchFieldValue")
        when (val result = getForecastWithCityName.getForecast(cityName)) {
            is Response.Success -> {
                Log.e("FavoritesViewModel", "fetchForecastWithCityName Response.Success")
                _searchCityState.value = SearchCityState.Success(result.data)
            }
            is Response.Error -> {
                Log.e("FavoritesViewModel", "fetchForecastWithCityName Response.Error")
                _searchCityState.value = SearchCityState.Error(exceptionStringRepository.exceptionMessage(result.message))
            }
        }
    }

    fun addMyCity(myCity: MyCity) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (!getSpecificCityUseCase.getSpecificCityUseCase(myCity.cityName)) {
                    addMyCityUseCase.addMyCity(myCity)
                    loadMyCities()
                } else {
                    Log.e("add city", "you have already added this city")
                }
            } catch (e: Exception) {
                Log.e("e", e.message.toString())
            }
        }
    }

    fun removeMyCity(cityName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                deleteMyCityUseCase.deleteMyCityUseCase(cityName)
                loadMyCities()
            } catch (e: Exception) {
                Log.e("e", e.message.toString())
            }
        }
    }

    // no internet connection -> load cities from database
    // internet connection -> update our cities
    private fun updateMyCity() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                getMyCityUseCase.getMyCity().forEach { myCity ->
                    when (val result = getForecastWithCityName.getForecast(myCity.cityName)) {
                        is Response.Success -> {
                            result.data?.let {
                                updateMyCityUseCase.updateMyCityUseCase(
                                    MyCity(
                                        temp = it.weatherList[0].weatherData.temp,
                                        latitude = it.cityDtoData.coordinate.latitude,
                                        longitude = it.cityDtoData.coordinate.longitude,
                                        cityName = it.cityDtoData.cityName,
                                        country = it.cityDtoData.country,
                                        description = it.weatherList[0].weatherStatus[0].description,
                                        weatherImage = WeatherType.getWeatherType(
                                            mainDescription = it.weatherList[0].weatherStatus[0].mainDescription,
                                            weatherDescription = it.weatherList[0].weatherStatus[0].description,
                                            HourConverter.convertHour(
                                                it.weatherList[0].date.substring(11, 13)
                                            ),
                                        )
                                    )
                                )
                                _myCitiesState.value =
                                    MyCitiesState.Success(getMyCityUseCase.getMyCity())
                            }
                        }
                        is Response.Error -> {
                            if (result.message == Constants.UNKNOWN_HOST) {
                                _myCitiesState.value =
                                    MyCitiesState.Success(getMyCityUseCase.getMyCity())
                            } else {
                                _myCitiesState.value = MyCitiesState.Error(exceptionStringRepository.exceptionMessage(result.message))
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("e", e.message.toString())
            }
        }
    }

    private fun checkSearchFieldValue(): Boolean {
        return searchFieldValue.isNotEmpty()
    }

    fun updateSearchField(input: String) {
        searchFieldValue = input
    }

    private fun isMyCitiesExist(): Boolean {
        return getMyCityUseCase.getMyCity().isNotEmpty()
    }
}

/*

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val getForecastWithCityName: GetForecastWithCityNameUseCase,
    private val getMyCityUseCase: GetMyCityUseCase,
    private val addMyCityUseCase: AddMyCityUseCase,
    private val deleteMyCityUseCase: DeleteMyCityUseCase,
    private val updateMyCityUseCase: UpdateMyCityUseCase,
    private val getSpecificCityUseCase: GetSpecificCityUseCase,
    private val exceptionStringRepository: ExceptionStringRepository
) : ViewModel() {

    private val _searchCityState = MutableStateFlow<SearchCityState>(SearchCityState.Loading)
    val searchCityState = _searchCityState.asStateFlow()

    private val _myCitiesState = MutableStateFlow<MyCitiesState>(MyCitiesState.Loading)
    val myCitiesState = _myCitiesState.asStateFlow()

    var searchFieldValue by mutableStateOf("")
        private set

    var isCitySearched by mutableStateOf(false)
        private set

    init {
        loadMyCities()
    }

    fun errorOnClick() {
        _searchCityState.value = SearchCityState.Success(null)
    }

    fun searchCityClick() {
        isCitySearched = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (checkSearchFieldValue()) {
                    val cityName = searchFieldValue
                    val response = getForecastWithCityName.getForecast(cityName)
                    handleResponse(response,
                        onSuccess = { forecast ->
                            _searchCityState.value = SearchCityState.Success(forecast)
                        },
                        onError = { errorMessage ->
                            _searchCityState.value = SearchCityState.Error(exceptionStringRepository.exceptionMessage(errorMessage))
                        }
                    )
                } else {
                    _searchCityState.value = SearchCityState.Error(exceptionStringRepository.exceptionMessage(Constants.FILL_FIELD))
                }
            } catch (e: Exception) {
                _searchCityState.value = SearchCityState.Error(exceptionStringRepository.exceptionMessage(e.message))
            }
        }
    }

    private fun loadMyCities() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (isMyCitiesExist()) {
                    updateMyCities()
                } else {
                    _myCitiesState.value = MyCitiesState.Success(emptyList())
                }
            } catch (e: Exception) {
                _myCitiesState.value = MyCitiesState.Error(exceptionStringRepository.exceptionMessage(e.message))
            }
        }
    }

    fun addMyCity(myCity: MyCity) {
        viewModelScope.launch(Dispatchers.IO) {
            if (!getSpecificCityUseCase.getSpecificCityUseCase(myCity.cityName)) {
                addMyCityUseCase.addMyCity(myCity)
                loadMyCities()
            } else {
                Log.e("add city", "City already added")
            }
        }
    }

    fun removeMyCity(cityName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteMyCityUseCase.deleteMyCityUseCase(cityName)
            loadMyCities()
        }
    }

    private suspend fun updateMyCities() {
        getMyCityUseCase.getMyCity().forEach { myCity ->
            val response = getForecastWithCityName.getForecast(myCity.cityName)
            handleResponse(response,
                onSuccess = { forecast ->
                    viewModelScope.launch(Dispatchers.IO) {
                        updateMyCityUseCase.updateMyCityUseCase(
                            MyCity(
                                temp = forecast.weatherList[0].weatherData.temp,
                                latitude = forecast.cityDtoData.coordinate.latitude,
                                longitude = forecast.cityDtoData.coordinate.longitude,
                                cityName = forecast.cityDtoData.cityName,
                                country = forecast.cityDtoData.country,
                                description = forecast.weatherList[0].weatherStatus[0].description,
                                weatherImage = WeatherType.getWeatherType(
                                    mainDescription = forecast.weatherList[0].weatherStatus[0].mainDescription,
                                    weatherDescription = forecast.weatherList[0].weatherStatus[0].description,
                                    HourConverter.convertHour(
                                        forecast.weatherList[0].date.substring(
                                            11,
                                            13
                                        )
                                    )
                                )
                            )
                        )
                    }
                    _myCitiesState.value = MyCitiesState.Success(getMyCityUseCase.getMyCity()) // обновление состояния списка городов
                },
                onError = { errorMessage ->
                    _myCitiesState.value = MyCitiesState.Error(exceptionStringRepository.exceptionMessage(errorMessage))
                }
            )
        }
    }

    private fun <T> handleResponse(
        response: Response<T>,
        onSuccess: (T) -> Unit,
        onError: (String) -> Unit
    ) {
        when (response) {
            is Response.Success -> {
                response.data?.let { data ->
                    onSuccess(data)
                } ?: onError(Constants.DATA_NOT_FOUND)
            }
            is Response.Error -> {
                onError(response.message ?: Constants.UNKNOWN_ERROR)
            }
        }
    }


    private fun checkSearchFieldValue(): Boolean {
        return searchFieldValue.isNotEmpty()
    }

    fun updateSearchField(input: String) {
        searchFieldValue = input
    }

    private fun isMyCitiesExist(): Boolean {
        return getMyCityUseCase.getMyCity().isNotEmpty()
    }
}*/
