package com.example.jetmerandom.data

import com.example.jetmerandom.data.domain.Flight
import com.google.android.gms.maps.model.LatLng

object DataSource {

    val cities = mutableListOf<String>()

    val flights = mutableListOf<Flight>()

    val flightsListed = mutableMapOf<String, List<Flight>>()

    val routesLocation = mutableMapOf<String,LatLng>()
}