package com.example.jetmerandom.data

import java.time.LocalDate

data class SearchUiState(

    val origin: String = "",

    val minPrice: Double = 0.0,

    val maxPrice: Double = 500.0,

    val maxTime: Int = 20,

    val qAdults: Int = 1,

    val qChilds: Int = 0,

    val startDate: LocalDate = LocalDate.now(),

    val endDate: LocalDate = LocalDate.now()

    //val sortBy: SortType,
)
