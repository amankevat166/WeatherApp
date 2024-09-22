package com.example.weatheraman

import Repository.WeatherRepo
import android.Manifest
import ViewModels.WeatherViewModel
import ViewModels.WeatherViewModelFactory
import WeatherApi.Constant
import WeatherApi.RetrofitInstance
import WeatherApi.WeatherService
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.weatheraman.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.Locale

class MainActivity : AppCompatActivity() {

    lateinit var bindng: ActivityMainBinding
    private val themeTitleList = arrayOf("Light","Dark","Auto (System Default)")
    lateinit var weatherViewModel: WeatherViewModel

    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()

        val sharedPreferenceManager = SharedPreferenceManager(this)
        val checkedTheme = sharedPreferenceManager.theme

        //hide status bar
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        bindng = DataBindingUtil.setContentView(this, R.layout.activity_main)

        var city = bindng.searchview.text.toString()
        var location = if (city.isNotEmpty()) city else "london"
        //search weather on basis of city when click on search icon
        bindng.searchIcon.setOnClickListener {
            hideKeyboard()

            val cityB = bindng.searchview.text.toString()
            getWeather(cityB)
            bindng.searchview.text.clear()

        }
        checkPermissions()


    }


    fun getWeather(city: String) {

        val repository = (application as WeatherApplication).weatherRepo
//        val weatherService = RetrofitInstance.getInstance().create(WeatherService::class.java)
//        val repository = WeatherRepo(weatherService)
        weatherViewModel = ViewModelProvider(
            this,
            WeatherViewModelFactory(repository)
        ).get(WeatherViewModel::class.java)
        weatherViewModel.getWeather(city)
        weatherViewModel.weatherLiveData.observe(this, Observer {

            if (it!= null) {
                bindng.temp.text = "${it!!.current.temp_c} C"
                bindng.location.text = it.location.name
                bindng.status.text = it.current.condition.text
                bindng.humidity.text = it.current.humidity.toString()
                bindng.wind.text = "${it.current.wind_kph.toString()} km/h"
                bindng.pressure.text = it.current.pressure_mb.toString()
                bindng.date.text = it.location.localtime.split(" ")[0]
                bindng.time.text = it.location.localtime.split(" ")[1]
            }
            else{
                Toast.makeText(this,"enter valid city name",Toast.LENGTH_SHORT).show()
            }
        })

    }


    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = currentFocus ?: View(this) // Get the currently focused view
        imm.hideSoftInputFromWindow(view.windowToken, 0) // Hide the keyboard
    }


    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        } else {
            getLocation()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation()
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

         fun getLocation() {
             fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

             if (ContextCompat.checkSelfPermission(
                     this,
                     Manifest.permission.ACCESS_FINE_LOCATION
                 ) == PackageManager.PERMISSION_GRANTED
             ) {
                 fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                     location?.let {
                         getCityName(it.latitude, it.longitude)
                     } ?: run {
                         Toast.makeText(this, "Unable to get location", Toast.LENGTH_SHORT).show()
                     }
                 }
             }
         }

              fun getCityName(latitude: Double, longitude: Double) {
                  val geocoder = Geocoder(this, Locale.getDefault())
                  val addressList: List<Address>?

                  try {
                      addressList = geocoder.getFromLocation(latitude, longitude, 1)
                      if (addressList != null && addressList.isNotEmpty()) {
                          val currentCity = addressList[0].locality
                          getWeather(currentCity)

                      } else {
                          Toast.makeText(this, "City not found", Toast.LENGTH_SHORT).show()
                      }
                  } catch (e: IOException) {
                      e.printStackTrace()
                      Toast.makeText(this, "Geocoder service not available", Toast.LENGTH_SHORT)
                          .show()
                  }
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