package com.example.jetmerandom.data

data class Data(
    val airlines: List<String>,
    val availability: Availability,
    val baglimit: Baglimit,
    val bags_price: BagsPrice,
    val booking_token: String,
    val cityCodeFrom: String,
    val cityCodeTo: String,
    val cityFrom: String,
    val cityTo: String,
    val conversion: Conversion,
    val countryFrom: CountryFrom,
    val countryTo: CountryTo,
    val distance: Double,
    val duration: Duration,
    val facilitated_booking_available: Boolean,
    val fare: Fare,
    val flyFrom: String,
    val flyTo: String,
    val has_airport_change: Boolean,
    val hidden_city_ticketing: Boolean,
    val id: String,
    val local_arrival: String,
    val local_departure: String,
    val nightsInDest: Any,
    val pnr_count: Int,
    val price: Int,
    val price_dropdown: PriceDropdown,
    val quality: Double,
    val route: List<Route>,
    val technical_stops: Int,
    val throw_away_ticketing: Boolean,
    val utc_arrival: String,
    val utc_departure: String,
    val virtual_interlining: Boolean
)