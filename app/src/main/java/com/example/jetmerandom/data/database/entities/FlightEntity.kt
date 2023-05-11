package com.example.jetmerandom.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.jetmerandom.data.flight.Duration
import com.example.jetmerandom.data.flight.Fare
import com.example.jetmerandom.data.flight.Flight
import com.example.jetmerandom.data.flight.Route


@Entity(tableName = "flight_table")
data class FlightEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val city_from: String,
    val city_to: String,
    val depDate: String,
    val retDate: String,
    val price: Int,
    val duration: String,
    val imageURL: String,
    val currency: String,
    val deep_link: String,
    val n_stops: Int,
    val n_passengers: Int
)

