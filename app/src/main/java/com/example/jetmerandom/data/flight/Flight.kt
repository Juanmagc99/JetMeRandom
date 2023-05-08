package com.example.jetmerandom.data.flight

data class Flight(
    val city_from: String,
    val city_to: String,
    val routes: List<Route>,
    val fare: Fare,
    val price: Int,
    val duration: Duration,
    val imageURL: String,
    val currency: String,
    val deep_link: String,
)
