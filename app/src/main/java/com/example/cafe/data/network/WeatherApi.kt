package com.example.cafe.data.network

import retrofit2.http.GET

interface WeatherApi {
    @GET("v1/forecast?latitude=55.75&longitude=37.62&current_weather=true")
    suspend fun getWeather(): WeatherResponse
}