package com.example.cafe.repository

import com.example.cafe.data.network.RetrofitInstance
import com.example.cafe.data.network.WeatherResponse

class WeatherRepository {
    suspend fun getWeather(): WeatherResponse? {
        return try {
            RetrofitInstance.api.getWeather()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}