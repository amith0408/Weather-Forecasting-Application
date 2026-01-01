package com.example.weather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Compress
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weather.viewmodel.WeatherViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.draw.paint
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Button
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.TextFieldDefaults
import com.example.weather.ui.theme.BlueJC
import com.example.weather.ui.theme.DarkBlueJC
import androidx.compose.material.icons.filled.Place
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.weather.R
import androidx.compose.ui.tooling.preview.Preview
import com.example.weather.ui.theme.WeatherTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.ui.draw.alpha
import com.example.weather.ui.theme.Purple80


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WeatherTheme {
                WeatherScreen()
            }
        }
    }
}

val lightGray =Color(0xFFF5F5F5)

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun WeatherPreview() {
    WeatherTheme {
        WeatherScreen()
    }
}


data class WeatherUIState(
    val backgroundColor: Color,
    val iconRes: Int
)

fun getUIState(condition: String?): WeatherUIState {
    return when (condition?.lowercase()) {
        "clear" -> WeatherUIState(Color(0xFF87CEEB), R.drawable.clear_sky)
        "clouds" -> WeatherUIState(Color(0xFFB0C4DE), R.drawable.cloudy)
        "overcast" -> WeatherUIState(Color(0xFF778899), R.drawable.over_cast)
        "drizzle" -> WeatherUIState(Color(0xFFADD8E6), R.drawable.rain_slight)
        "rain" -> WeatherUIState(Color(0xFF4682B4), R.drawable.rain_heavy)
        "thunderstorm" -> WeatherUIState(Color(0xFF2F4F4F), R.drawable.thunder_storm)
        "snow" -> WeatherUIState(Color(0xFFF0F8FF), R.drawable.snow_fall)
        "fog", "mist" -> WeatherUIState(Color(0xFFDCDCDC), R.drawable.fog)
        else -> WeatherUIState(Color(0xFF87CEEB), R.drawable.mainly_clear)
    }
}

@Composable
fun WeatherScreen() {
    val viewModel: WeatherViewModel = viewModel()
    val weatherData by viewModel.weatherData.collectAsState()
    var city by remember { mutableStateOf("") }
    val apiKey = "e2cda34d442975b44afa26be9cc4e1e9"

    // Get the dynamic color and icon based on weather condition
    val uiState = getUIState(weatherData?.weather?.get(0)?.main)

    Box(
        Modifier
            .fillMaxSize()
            .background(uiState.backgroundColor) // DYNAMIC BACKGROUND COLOR
    ) {
        // Subtle background icon overlay
        Icon(
            painter = painterResource(id = uiState.iconRes),
            contentDescription = null,
            modifier = Modifier
                .size(350.dp)
                .align(Alignment.TopEnd)
                .offset(x = 80.dp, y = (-20).dp)
                .alpha(0.15f),
            tint = Color.White
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item(span = { GridItemSpan(2) }) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Spacer(modifier = Modifier.height(100.dp))
                    OutlinedTextField(
                        value = city,
                        onValueChange = { city = it },
                        label = { Text("Enter City") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(30.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White.copy(alpha = 0.9f),
                            unfocusedContainerColor = Color.White.copy(alpha = 0.8f)
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { viewModel.fetchWeather(city, apiKey) },
                        colors = ButtonDefaults.buttonColors(Color(0xFF673AB7),
                            contentColor = Color.White
                        )
                    ) {
                        Text("Check Weather",
                            color = Color.Black)
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }

            weatherData?.let { data ->
                // Using your specific XML files for cards
                item { WeatherCard("City", data.name, painterResource(id = R.drawable.mainly_clear)) }
                item { WeatherCard("Temp", "${data.main.temp}°C", painterResource(id = R.drawable.thermostat)) }
                item { WeatherCard("Wind", "${data.wind.speed} m/s", painterResource(id = R.drawable.wind_ic)) }
                item { WeatherCard("Condition", data.weather[0].main, painterResource(id = uiState.iconRes)) }
                item { WeatherCard("Humidity", "${data.main.humidity}%", painterResource(id = R.drawable.rain_slight)) }
                item { WeatherCard("Pressure", "${data.main.pressure} hPa", painterResource(id = R.drawable.outline_compress_24)) }
            }
        }
    }
}

@Composable
fun WeatherCard(label: String, value: String, icon: androidx.compose.ui.graphics.painter.Painter) {
    Card(
        modifier = Modifier.padding(4.dp).height(140.dp),
        colors = CardDefaults.cardColors(Color(0xFFF8F9FA).copy(alpha = 0.85f)),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp).fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = icon, // Now using Painter for your XML files
                contentDescription = null,
                tint = DarkBlueJC,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = label, color = Color(0xFF708090), fontSize = 16.sp)
            Text(text = value, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color(0xFF263238))
        }
    }
}


