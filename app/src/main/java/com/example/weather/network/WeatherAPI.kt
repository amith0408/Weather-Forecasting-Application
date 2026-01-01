package com.example.weather.network

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import WeatherResponse
interface WeatherAPI {
    @GET("weather")
    suspend fun getcurrentWeather(
        @Query("q") city: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): Response<WeatherResponse>

    companion object {
        private const val BASE_URL="https://api.openweathermap.org/data/2.5/"
        fun create(): WeatherAPI{
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(WeatherAPI::class.java)
        }
    }
}