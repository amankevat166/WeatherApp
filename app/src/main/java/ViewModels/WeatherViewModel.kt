package ViewModels

import Repository.WeatherRepo
import WeatherApi.Constant
import WeatherModel.WeatherModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WeatherViewModel(private val repository:WeatherRepo):ViewModel() {
    private val _weatherLiveData = MutableLiveData<WeatherModel?>()
    val weatherLiveData: LiveData<WeatherModel?>
        get() = _weatherLiveData


    fun getWeather(city:String){
        viewModelScope.launch(Dispatchers.IO) {
          val data = repository.getWeather(Constant.API_KEY,city)
            _weatherLiveData.postValue(data)
       }
    }
//    init {
//        viewModelScope.launch(Dispatchers.IO) {
//            repository.getWeather(Constant.API_KEY,city)
//        }
//    }
   // val weatherLiveData = repository.weatherLiveData

}