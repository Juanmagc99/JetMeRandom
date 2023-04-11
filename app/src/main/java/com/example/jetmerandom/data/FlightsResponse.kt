package com.example.jetmerandom.data

data class FlightsResponse(
    val _results: Int,
    val all_stopover_airports: List<Any>,
    val currency: String,
    val `data`: List<Data>,
    val fx_rate: Int,
    val search_id: String,
    val search_params: SearchParams,
    val sort_version: Int
)