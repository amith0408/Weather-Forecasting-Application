data class WeatherResponse(
    val name: String,
    val main: Main,
    val weather: List<Weather>,
    val wind: Wind,        // Added to get wind speed
    val visibility: Int    // Added for visibility (returned in meters)
)

data class Main(
    val temp: Float,
    val feels_like: Float, // Added for "Feels Like"
    val humidity: Int,
    val pressure: Int      // Added for atmospheric pressure
)

data class Weather(
    val description: String,
    val main: String,
    val icon: String       // Added icon ID for future use
)

data class Wind(
    val speed: Float       // Added wind speed field
)