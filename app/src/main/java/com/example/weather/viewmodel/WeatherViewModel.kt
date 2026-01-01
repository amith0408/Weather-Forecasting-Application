package com.example.weather.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.network.WeatherAPI
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import WeatherResponse
class WeatherViewModel: ViewModel() {
    private val _weatherData = MutableStateFlow<WeatherResponse?>(null)
    val weatherData: StateFlow<WeatherResponse?> = _weatherData
    private val weatherAPI = WeatherAPI.Companion.create()
    fun fetchWeather(city: String,apikey: String){
        viewModelScope.launch {
            try {
                val response = weatherAPI.getcurrentWeather(city, apikey)
                // CHANGE: Check for success and use .body()
                if (response.isSuccessful) {
                    _weatherData.value = response.body()
                } else {
                    // Log the error (e.g., city not found)
                    println("Error: ${response.code()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}