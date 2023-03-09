package com.example.jetmerandom.data

data class FligthsResponse(
    val currency: String,
    val data: List<Data>,
    val fx_rate: Int,
    val search_id: String,
    val search_params: SearchParams
)