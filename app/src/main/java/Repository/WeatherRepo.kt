package Repository

import WeatherApi.WeatherService
import WeatherModel.WeatherModel

class WeatherRepo(private val weatherService: WeatherService) {

//    private val _weatherLiveData = MutableLiveData<WeatherModel>()
//    val weatherLiveData:LiveData<WeatherModel>
//        get() = _weatherLiveData

    suspend fun getWeather(apiKey:String,city:String):WeatherModel?{
        val result = weatherService.getWeather(apiKey,city)
        return if (result.isSuccessful){
            result.body()
        }
        else{
            null
        }
    }
}