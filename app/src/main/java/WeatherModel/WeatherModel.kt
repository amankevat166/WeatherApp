package WeatherModel

import androidx.room.Entity
import androidx.room.PrimaryKey


data class WeatherModel(

    val current: Current,
    val location: Location
)