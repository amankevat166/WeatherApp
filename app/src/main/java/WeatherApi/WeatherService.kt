package WeatherApi

import WeatherModel.WeatherModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query



interface WeatherService {
    //a1943ca3269c4d84a1452257242009
    //https://api.weatherapi.com/v1/current.json?key=a1943ca3269c4d84a1452257242009&q=delhi&aqi=no
    @GET("/v1/current.json")
    suspend fun getWeather(@Query("key")key:String, @Query("q")city:String):Response<WeatherModel>

}