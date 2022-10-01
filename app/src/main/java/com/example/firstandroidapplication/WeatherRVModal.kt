package com.example.firstandroidapplication

class WeatherRVModal (time: String, temperature: String, icon: String, windSpeed: String,){
    var time: String
    var temperature: String
    var windSpeed: String
    var icon: String

    init{
        this.time = time
        this.temperature = temperature
        this.icon = icon
        this.windSpeed = windSpeed
    }
}