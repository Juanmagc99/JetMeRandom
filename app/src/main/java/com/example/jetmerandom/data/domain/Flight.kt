package com.example.jetmerandom.data.domain

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.jetmerandom.data.flight.Duration
import com.example.jetmerandom.data.flight.Fare
import com.example.jetmerandom.data.flight.Route

data class Flight(
    val id: Int = 0,
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
