package com.example.jetmerandom.data

import com.example.jetmerandom.data.database.entities.FlightEntity


data class FlightLikedUiState(
    val flightsLiked: List<FlightEntity> = emptyList()
)
