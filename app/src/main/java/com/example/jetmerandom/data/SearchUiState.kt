package com.example.jetmerandom.data

import com.example.jetmerandom.data.flight.Flight
import java.time.LocalDate
import java.time.LocalTime

data class SearchUiState(

    val origin: String = "",

    val dest: String = "",

    val minPrice: Float = 10.0f,

    val maxPrice: Float = 500.0f,

    val minTime: Int = 2,

    val qAdults: Int = 1,

    val qChilds: Int = 0,

    val startDate: LocalDate = LocalDate.now(),

    val endDate: LocalDate = LocalDate.now().plusDays(20),

    val isDirect: Boolean = true,

    val maxHours: Int = 10,

    val depTime: LocalTime = LocalTime.of(23,59),

    val arrTime: LocalTime = LocalTime.of(23,59),

    val rdepTime: LocalTime = LocalTime.of(23,59),

    val rarrTime: LocalTime = LocalTime.of(23,59),


    /*
    *
    *
    *
    *
    *
    *
    * */

    val cabinType: String = "M",

    val checkDates: Boolean = true,

    val checkCityExists: Boolean = true,

    val checkCityIsntBlank: Boolean = true,

    val checkPassengers: Boolean = true,

    val checkHours: Boolean = true,

    val checkDays: Boolean = true,

    val flight: Flight? = null


)
