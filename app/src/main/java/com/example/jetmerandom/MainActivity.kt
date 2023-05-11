package com.example.jetmerandom

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.jetmerandom.data.DataSource.cities
import com.example.jetmerandom.ui.LikedFlightViewModel
import com.example.jetmerandom.ui.SearchViewModel
import com.example.jetmerandom.ui.screens.JetMeRandomAppTotal

import com.example.jetmerandom.ui.theme.JetMeRandomTheme
import dagger.hilt.android.AndroidEntryPoint
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.reflect.Modifier

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        chargeLocations()

        val searchViewModel: SearchViewModel by viewModels()
        val likedFlightViewModel: LikedFlightViewModel by viewModels()

        setContent {
            JetMeRandomTheme {
                JetMeRandomAppTotal(
                    searchViewModel = searchViewModel,
                    likedFlightViewModel = likedFlightViewModel
                )
            }
        }
    }

    fun chargeLocations(){
        val csvInput = InputStreamReader(assets.open("airports.csv"))
        val reader = BufferedReader(csvInput)

        reader.readLine()
        var lines: List<String> = reader.readLines()

        lines.forEach {
            val row : List<String> = it.split(";")
            val city = row[10]
            val code = row[13]
            if(row[7] == "EU" && code != "" && city != "" && (row[8] == "GB" || row[8] == "IE")){
                cities.add("$code - $city")
            }
        }

    }

}


