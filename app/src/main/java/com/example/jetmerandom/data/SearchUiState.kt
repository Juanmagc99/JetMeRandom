package com.example.jetmerandom.data

import com.example.jetmerandom.data.flight.Flight
import java.time.LocalDate

data class SearchUiState(

    val origin: String = "",

    val minPrice: Float = 10.0f,

    val maxPrice: Float = 500.0f,

    val minTime: Int = 2,

    val qAdults: Int = 1,

    val qChilds: Int = 0,

    val startDate: LocalDate = LocalDate.now(),

    val endDate: LocalDate = LocalDate.now().plusDays(20),

    val isDirect: Boolean = true,

    val checkDates: Boolean = true,

    val checkCityExists: Boolean = true,

    val checkCityIsntBlank: Boolean = true,

    val checkPassengers: Boolean = true,

    val checkHours: Boolean = true,

    val flight: Flight? = null


)
