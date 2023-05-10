package com.example.jetmerandom.data.flight

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.jetmerandom.data.database.entities.FlightEntity
import com.example.jetmerandom.data.flight.Duration
import com.example.jetmerandom.data.flight.Fare
import com.example.jetmerandom.data.flight.Route

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
    val liked: Boolean = false
)

fun Flight.toEntity() = FlightEntity(
    city_from = city_from,
    city_to = city_to,
    currency = currency,
    deep_link = deep_link,
    imageURL = imageURL,
    price = price,
    duration = java.time.Duration.ofSeconds(duration.departure.toLong()).toString(),
    depDate = routes[0].utc_departure.split("T")[1].dropLast(8),
    retDate = routes.last().utc_departure.split("T")[1].dropLast(8)
)