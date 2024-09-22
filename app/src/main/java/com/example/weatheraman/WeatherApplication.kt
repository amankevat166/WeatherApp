package com.example.weatheraman

import Repository.WeatherRepo

import WeatherApi.RetrofitInstance
import WeatherApi.WeatherService
import android.app.Application

class WeatherApplication : Application() {

    lateinit var weatherRepo: WeatherRepo

    override fun onCreate() {
        super.onCreate()
        initialize()
    }

    private fun initialize() {
        val weatherService = RetrofitInstance.getInstance().create(WeatherService::class.java)
         weatherRepo = WeatherRepo(weatherService)
    }
}