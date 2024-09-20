package com.example.weatheraman

import Repository.WeatherRepo
import ViewModels.WeatherViewModel
import ViewModels.WeatherViewModelFactory
import WeatherApi.Constant
import WeatherApi.RetrofitInstance
import WeatherApi.WeatherService
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.weatheraman.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    lateinit var bindng: ActivityMainBinding

    lateinit var weatherViewModel: WeatherViewModel
   lateinit  var searchText:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
       bindng = DataBindingUtil. setContentView(this,R.layout.activity_main)

        //search weather on basis of city when click on search icon
        bindng.searchIcon.setOnClickListener {
            createViewModel(bindng.searchview.text.toString())
               // getWeather()

        }
        //by default mention city name data is shown
        //getWeather()
    }

    fun createViewModel(city:String){
        val weatherService = RetrofitInstance.getInstance().create(WeatherService::class.java)
        val repository = WeatherRepo(weatherService)
        weatherViewModel = ViewModelProvider(this,WeatherViewModelFactory(repository,city)).get(WeatherViewModel::class.java)
        weatherViewModel.weatherLiveData.observe(this, Observer {
            bindng.temp.text = "${it.current.temp_c.toString()} C"
            bindng.location.text = it.location.name
            bindng.status.text = it.current.condition.text
            bindng.humidity.text = it.current.humidity.toString()
            bindng.wind.text = "${it.current.wind_kph.toString()} km/h"
            bindng.pressure.text = it.current.pressure_mb.toString()
            bindng.date.text = it.location.localtime.split(" ")[0]
            bindng.time.text = it.location.localtime.split(" ")[1]
        })
    }










//    private fun getWeather() {
//        val weatherApi = RetrofitInstance.weatherApi //store retrofit instance and service of api
//
//        //launch coroutine bcz of suspend function
//                CoroutineScope(Dispatchers.IO).launch {
//
//                     searchText = bindng.searchview.text.toString()
//                    val location = if (searchText.isNotEmpty()) searchText else "london" //if text is empty then default is london
//                    val response = weatherApi.getWeather(Constant.API_KEY, location) //called get weather method and pass parameter
//                    val result = response.body()
//
//                    //bind the data with views
//                    if (response.isSuccessful) {
//                        withContext(Dispatchers.Main) {
//                            bindng.temp.text = "${result!!.current.temp_c.toString()} C"
//                            bindng.location.text = result!!.location.name
//                            bindng.status.text = result!!.current.condition.text
//                            bindng.humidity.text = result!!.current.humidity.toString()
//                            bindng.wind.text = "${result!!.current.wind_kph.toString()} km/h"
//                            bindng.pressure.text = result!!.current.pressure_mb.toString()
//                            bindng.date.text = result!!.location.localtime.split(" ")[0]
//                            bindng.time.text = result!!.location.localtime.split(" ")[1]
//
//                        }
//
//                    } else {
//                        Log.d("aman", response.message())
//
//                    }
//                }
//    }
//

}