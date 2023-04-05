package com.example.jetmerandom

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.jetmerandom.API.APIService
import com.example.jetmerandom.data.DataSource
import com.example.jetmerandom.data.DataSource.flights
import com.example.jetmerandom.data.DataSource.routesLocation
import com.example.jetmerandom.data.Flight
import com.example.jetmerandom.data.SearchUiState
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.util.function.Predicate
import kotlin.streams.toList

class SearchViewModel: ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()


    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.tequila.kiwi.com/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getFlightRoute() {
        routesLocation.clear()
        val flight = uiState.value.flight
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(APIService::class.java)
            for (r in flight!!.routes){
                val location_departure_call = call.getCitiesLocation(r.cityCodeFrom)
                val location_arrival_call = call.getCitiesLocation(r.cityCodeTo)
                println(location_arrival_call)
                if (location_departure_call.isSuccessful && location_arrival_call.isSuccessful){
                    print("Funciona")
                    val locationDeparture = location_departure_call.body()?.data?.get(0)
                    val locationArrival = location_arrival_call.body()?.data?.get(0)
                    if (locationDeparture != null && locationArrival != null) {
                        routesLocation.add(LatLng(locationDeparture.latitude,locationDeparture.longitude))
                        routesLocation.add(LatLng(locationArrival.latitude,locationArrival.longitude))
                    }
                }
            }
            println(routesLocation)
        }
    }

    fun getFlights(onNextButtonClicked: () -> Unit = {}) {
        flights.clear()
        CoroutineScope(Dispatchers.IO).launch{
            val call = getRetrofit().create(APIService::class.java).getFlights(
                apiKey = "dzH-q3MBLRRtJFFmcKPvBHqML_YdCEfB",
                fly_from = "MAD",
                date_from = "09/05/2023",
                date_to = "18/05/2023",
                return_from = "20/05/2023",
                return_to = "28/05/2023",
                flight_type = "round",
                nights_in_dst_from = 2,
                nights_in_dst_to = 20,
                limit = 15,
            )
            if (call.isSuccessful){
                println("LLamada exitosa")
                val flights_to_add = call.body()?.data
                val flights_filtered = flights_to_add
                    ?.stream()
                    ?.filter(Predicate { f -> f.availability.seats >= uiState.value.qChilds + uiState.value.qAdults })
                    ?.toList()

                if (flights_filtered != null) {
                    if(flights_filtered.isNotEmpty()){
                        val currency = call.body()!!.currency
                        for (f in flights_filtered) {
                            val call_image = getRetrofit().create(APIService::class.java)
                                .getImage(f.cityTo.lowercase())
                            var imageURL = "https://www.salonlfc.com/wp-content/uploads/2018/01/image-not-found-1-scaled.png"
                            println(call_image)
                            if (call_image.isSuccessful){
                                imageURL = call_image.body()?.photos?.get(0)?.image?.mobile.toString()
                            }
                            val flight = Flight(
                                city_from = f.cityFrom,
                                city_to = f.cityTo,
                                duration = f.duration,
                                fare = f.fare,
                                routes = f.route,
                                price = f.price,
                                imageURL = imageURL,
                                currency = currency
                            )
                            flights.add(flight)
                        }
                        withContext(Dispatchers.Main){
                            onNextButtonClicked()
                        }
                    }
                }

            } else {
                println(call)
                println("Llamada erronea")
            }
        }
    }

    fun checkDates(){
        val currentState = _uiState.value
        val start = uiState.value.startDate
        val end = uiState.value.endDate

        if (start.isBefore(end) || start.isEqual(end)) {
            _uiState.value = currentState.copy(checkDates = true)
        } else {
            _uiState.value = currentState.copy(checkDates = false)
        }
    }

    fun checkAndSearch(context: Context){

    }

    fun checkPassengers(){
        val currentState = _uiState.value
        if (uiState.value.qAdults + uiState.value.qChilds > 0){
            _uiState.value = currentState.copy(checkPassengers = true)
        } else {
            _uiState.value = currentState.copy(checkPassengers = false)
        }
    }

    fun checkOrigin(){
        val currentState = _uiState.value
        val actualCity = uiState.value.origin

        if (!DataSource.cities.contains(actualCity) && actualCity.isNotBlank()){
            _uiState.value = currentState.copy(checkCityExists = false)
        } else {
            _uiState.value = currentState.copy(checkCityExists = true)
        }

        if (actualCity.isBlank()){
            _uiState.value = currentState.copy(checkCityIsntBlank = true)
        } else {
            _uiState.value = currentState.copy(checkCityIsntBlank = false)
        }
    }

    fun checkTime(){
        val currentState = _uiState.value
        val maxTime = uiState.value.maxTime

        if(maxTime <= 0){
            _uiState.value = currentState.copy(checkHours = false)
        } else {
            _uiState.value = currentState.copy(checkHours = true)
        }
    }

    fun setFligthDetails(flight: Flight){
        val currentState = _uiState.value

        _uiState.value = currentState.copy(flight = flight)
    }
    fun setDate(date: LocalDate, flag: String) {
        val currentState = _uiState.value
        if (flag == "Start"){
            _uiState.value = currentState.copy(startDate = date)
        } else {
            _uiState.value = currentState.copy(endDate = date)
        }

        checkDates()
    }

    fun setMaxHours(maxTime: Int){
        val currentState = _uiState.value
        _uiState.value = currentState.copy(maxTime = maxTime)

        checkTime()
    }

    fun setPriceRange(priceRange: ClosedFloatingPointRange<Float>){
        val currentState = _uiState.value
        val minPrice = priceRange.start
        val maxPrice = priceRange.endInclusive
        _uiState.value = currentState.copy(minPrice = minPrice)
        _uiState.value = currentState.copy(maxPrice = maxPrice)
    }

    fun setOrigin(origin: String) {
        val currentState = _uiState.value
        _uiState.value = currentState.copy(origin = origin)

        checkOrigin()
    }

    fun setIsDirect(isDirect: Boolean) {
        val currentState = _uiState.value
        _uiState.value = currentState.copy(isDirect = isDirect)
    }

    fun setPassenger(nPassenger: Int, flag: String) {
        val currentState = _uiState.value
        if (flag == "Adult"){
            _uiState.value = currentState.copy(qAdults = nPassenger)
        } else {
            _uiState.value = currentState.copy(qChilds = nPassenger)
        }

        checkPassengers()
    }


}