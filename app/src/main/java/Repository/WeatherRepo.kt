package Repository

import WeatherApi.WeatherService
import WeatherModel.WeatherModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class WeatherRepo(private val weatherService: WeatherService) {

    private val _weatherLiveData = MutableLiveData<WeatherModel>()
    val weatherLiveData:LiveData<WeatherModel>
        get() = _weatherLiveData

    suspend fun getWeather(apiKey:String,city:String){
        val result = weatherService.getWeather(apiKey,city)
        if (result?.body() != null){
            _weatherLiveData.postValue(result.body())
        }
    }
}