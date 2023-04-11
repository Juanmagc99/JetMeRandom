package com.example.jetmerandom

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.jetmerandom.data.DataSource.cities

import com.example.jetmerandom.ui.theme.JetMeRandomTheme
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        chargeLocations()

        setContent {
            JetMeRandomTheme {
                JetMeRandomApp()
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
            if(row[7] == "EU" && code != "" && city != "" && row[8] == "GB"){
                cities.add("$code - $city")
            }
        }

    }

}


