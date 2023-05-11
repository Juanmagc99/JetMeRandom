package com.example.jetmerandom.ui

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetmerandom.API.APIService
import com.example.jetmerandom.R
import com.example.jetmerandom.data.DataSource
import com.example.jetmerandom.data.DataSource.flights
import com.example.jetmerandom.data.DataSource.flightsListed
import com.example.jetmerandom.data.DataSource.routesLocation
import com.example.jetmerandom.data.flight.Flight
import com.example.jetmerandom.data.SearchUiState
import com.example.jetmerandom.domain.FlightsUseCases
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
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
import java.time.LocalTime
import java.time.Period
import java.util.function.Predicate
import java.util.stream.Collectors
import javax.inject.Inject
import kotlin.streams.toList

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val flightsUseCases: FlightsUseCases
): ViewModel(){

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()


    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.tequila.kiwi.com/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun onLikedFlight(flight: Flight){
        viewModelScope.launch {
            flightsUseCases.insertFlight(flight)
        }

    }

    fun getFlightRoute(onDetailsButtonClicked: () -> Unit = {}) {
        routesLocation.clear()
        val flight = uiState.value.flight
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(APIService::class.java)
            for (r in flight!!.routes){
                val location_departure_call = call.getCitiesLocation(r.cityFrom.replace(","," "))
                println(location_departure_call)
                val location_arrival_call = call.getCitiesLocation(r.cityTo.replace(","," "))
                println(location_arrival_call)
                if (location_departure_call.isSuccessful && location_arrival_call.isSuccessful){
                    print("LLamada exitosa a position")
                    val locationDeparture = location_departure_call.body()?.data?.get(0)
                    val locationArrival = location_arrival_call.body()?.data?.get(0)
                    if (locationDeparture != null && locationArrival != null) {
                        routesLocation[r.cityFrom] = LatLng(locationDeparture.latitude,locationDeparture.longitude)
                        routesLocation[r.cityTo] = LatLng(locationArrival.latitude,locationArrival.longitude)
                    }
                    withContext(Dispatchers.Main){
                        onDetailsButtonClicked()
                    }
                }else {
                    println("\n\nFALLO EN EL SERVIDOR NO SE HAN ENCONTRADO LAS COORDENADAS\n\n")
                }
            }
            println(routesLocation)
        }
    }

    fun getFlights(onNextButtonClicked: () -> Unit = {}, mContext: Context) {
        flights.clear()
        makeAToast(R.string.searching_flights,mContext)
        println(uiState.value.maxHours)
        CoroutineScope(Dispatchers.IO).launch{
            val call = getRetrofit().create(APIService::class.java).getFlights(
                apiKey = "dzH-q3MBLRRtJFFmcKPvBHqML_YdCEfB",
                fly_from = uiState.value.origin.split("-")[0].dropLast(1),
                date_from = formatDate(uiState.value.startDate.toString()),
                date_to = formatDate(uiState.value.endDate.minusDays(uiState.value.minTime.toLong()).toString()),
                return_from = formatDate(uiState.value.startDate.plusDays(uiState.value.minTime.toLong()).toString()),
                return_to = formatDate(uiState.value.endDate.toString()),
                flight_type = "round",
                nights_in_dst_from = uiState.value.minTime,
                nights_in_dst_to = Period.between(uiState.value.startDate,uiState.value.endDate).days,
                price_from = uiState.value.minPrice.toInt(),
                price_to = uiState.value.maxPrice.toInt(),
                max_stopovers = if (uiState.value.isDirect) 0 else {2},
                adults = uiState.value.qAdults,
                children = uiState.value.qChilds,
                dtime_to = uiState.value.depTime.toString(),
                atime_to = uiState.value.arrTime.toString(),
                ret_dtime_to = uiState.value.rdepTime.toString(),
                ret_atime_to = uiState.value.rarrTime.toString(),
                selected_cabins = uiState.value.cabinType,
                max_fly_duration = uiState.value.maxHours,
                limit = 100,
            )
            if (call.isSuccessful){
                println(call)
                val flights_to_add = call.body()?.data
                val flights_filtered = flights_to_add
                    ?.stream()
                    ?.filter(Predicate { f -> f.availability.seats >= uiState.value.qChilds + uiState.value.qAdults })
                    ?.toList()

                if (flights_filtered != null) {
                    if(flights_filtered.isNotEmpty()){
                        var setDestinations = mutableSetOf<String>()
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
                                currency = currency,
                                deep_link = f.deep_link,
                                n_passengers = uiState.value.qChilds + uiState.value.qAdults
                            )
                            setDestinations.add(f.cityTo)
                            flights.add(flight)
                            procesingFlights(flights = flights, destinations = setDestinations)
                        }
                        withContext(Dispatchers.Main){
                            onNextButtonClicked()
                        }
                    }
                } else {
                    withContext(Dispatchers.Main){
                        makeAToast(R.string.not_found,mContext)
                    }
                }
            } else {
                println(call)
                withContext(Dispatchers.Main){
                    makeAToast(R.string.wrong_message,mContext)
                }
            }
        }
    }

    fun procesingFlights(flights: List<Flight>, destinations: Set<String>){
        flightsListed.clear()
        for (d in destinations){
            flightsListed[d] = flights
                .stream()
                .filter { f -> f.city_to == d }
                .collect(Collectors.toList())
        }
    }

    fun makeAToast(idString:Int, mContext: Context){
        Toast.makeText(mContext,idString, Toast.LENGTH_LONG).show()
    }

    fun checkDates(){
        val currentState = _uiState.value
        val start = uiState.value.startDate
        val end = uiState.value.endDate


        if (end.minusDays(uiState.value.minTime.toLong()) <= start){
            _uiState.value = currentState.copy(checkDates = false)
        }else {
            _uiState.value = currentState.copy(checkDates = true)
        }
    }


    fun formatDate(date: String): String {
        val dateSplit = date.split("-")
        return dateSplit[2] + "/" + dateSplit[1] + "/" + dateSplit[0]
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

    fun setCabins(cabinType: String){
        val currentState = _uiState.value
        _uiState.value = currentState.copy(cabinType = cabinType)
    }

    fun setMinNights(minTime: Int){
        val currentState = _uiState.value
        if (minTime >= 0){
            _uiState.value = currentState.copy(minTime = minTime, checkDays = true)
        } else {
            _uiState.value = currentState.copy(checkDays = false)
        }


        checkDates()
    }

    fun setMaxHours(maxHours: Int){
        val currentState = _uiState.value
        if (maxHours >= 0){
            _uiState.value = currentState.copy(maxHours = maxHours,checkHours = true)
        } else {
            _uiState.value = currentState.copy(checkHours = false)
        }

    }

    fun setPriceRange(start:Float, end:Float){
        val currentState = _uiState.value
        _uiState.value = currentState.copy(minPrice = start, maxPrice = end)
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

    fun setDepTime(depTime: LocalTime){
        val currentState = _uiState.value
        _uiState.value = currentState.copy(depTime = depTime)
    }

    fun setArrTime(arrTime: LocalTime){
        val currentState = _uiState.value
        _uiState.value = currentState.copy(arrTime = arrTime)
    }

    fun setrDepTime(depTime: LocalTime){
        val currentState = _uiState.value
        _uiState.value = currentState.copy(rdepTime = depTime)
    }

    fun setrArrTime(arrTime: LocalTime){
        val currentState = _uiState.value
        _uiState.value = currentState.copy(rarrTime = arrTime)
    }


}