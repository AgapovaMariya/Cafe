package com.example.cafe.domain.usecases

import com.example.cafe.data.network.WeatherResponse
import com.example.cafe.repository.WeatherRepository

class GetWeatherUseCase(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(): WeatherResponse? {
        return repository.getWeather()
    }
}