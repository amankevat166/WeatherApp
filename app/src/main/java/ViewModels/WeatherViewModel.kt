package ViewModels

import Repository.WeatherRepo
import WeatherApi.Constant
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WeatherViewModel(private val repository:WeatherRepo, private val city:String):ViewModel() {

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getWeather(Constant.API_KEY,city)
        }
    }
    val weatherLiveData = repository.weatherLiveData

}