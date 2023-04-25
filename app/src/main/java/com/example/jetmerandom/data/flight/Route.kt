package com.example.jetmerandom.data.flight

data class Route(
    val airline: String,
    val bags_recheck_required: Boolean,
    val cityCodeFrom: String,
    val cityCodeTo: String,
    val cityFrom: String,
    val cityTo: String,
    val combination_id: String,
    val equipment: String,
    val fare_basis: String,
    val fare_category: String,
    val fare_classes: String,
    val fare_family: String,
    val flight_no: Int,
    val flyFrom: String,
    val flyTo: String,
    val guarantee: Boolean,
    val id: String,
    val local_arrival: String,
    val local_departure: String,
    val operating_carrier: String,
    val operating_flight_no: String,
    val `return`: Int,
    val utc_arrival: String,
    val utc_departure: String,
    val vehicle_type: String,
    val vi_connection: Boolean
)