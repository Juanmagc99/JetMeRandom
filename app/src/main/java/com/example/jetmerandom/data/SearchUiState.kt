package com.example.jetmerandom.data

import java.time.LocalDate

data class SearchUiState(

    val origin: String = "",

    val minPrice: Float = 10.0f,

    val maxPrice: Float = 500.0f,

    val maxTime: Int = 20,

    val qAdults: Int = 1,

    val qChilds: Int = 0,

    val startDate: LocalDate = LocalDate.now(),

    val endDate: LocalDate = LocalDate.now(),

    val isDirect: Boolean = true

    //val sortBy: SortType,
)
