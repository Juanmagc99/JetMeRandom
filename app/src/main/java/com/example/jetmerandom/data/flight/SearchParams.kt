package com.example.jetmerandom.data.flight

data class SearchParams(
    val flyFrom_type: String,
    val seats: Seats,
    val to_type: String
)