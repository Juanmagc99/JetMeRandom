package com.example.jetmerandom.domain

import com.example.jetmerandom.R
import com.example.jetmerandom.data.DataSource
import com.example.jetmerandom.data.database.FlightRepository
import com.example.jetmerandom.data.database.entities.FlightEntity
import com.example.jetmerandom.data.flight.Flight
import com.example.jetmerandom.data.flight.toEntity
import com.example.jetmerandom.data.network.APIClients
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.util.function.Predicate
import java.util.stream.Collectors
import javax.inject.Inject
import kotlin.streams.toList

class FlightsUseCases @Inject constructor(
    private val repository: FlightRepository
) {
    suspend fun getLikedFlights(): List<FlightEntity> {
        return repository.getAllFlightsFromDB()
    }

    suspend fun insertFlight(flight: Flight) {
        return repository.insertFlight(flight.toEntity())
    }

    suspend fun deleteFlight(flightEntity: FlightEntity) {
        return repository.deleteFlightFromDB(flightEntity)
    }

    suspend fun getFlightsNet(
        fly_from: String,
        date_from: String,
        date_to: String,
        return_to: String,
        return_from: String,
        limit: Int,
        nights_in_dst_from: Int,
        nights_in_dst_to: Int,
        flight_type: String,
        ret_from_diff_airport: Int = 0,
        ret_to_diff_airport: Int = 0,
        price_from: Int,
        price_to: Int,
        max_stopovers: Int,
        adults: Int,
        children: Int,
        selected_cabins: String,
        dtime_to: String,
        atime_to: String,
        ret_dtime_to: String,
        ret_atime_to: String,
        max_fly_duration: Int,
    ){
        val call = repository.getFlightsFromNet(
            fly_from = fly_from,
            date_from = date_from,
            date_to = date_to,
            return_from = return_from,
            return_to = return_to,
            flight_type = "round",
            nights_in_dst_from = nights_in_dst_from,
            nights_in_dst_to = nights_in_dst_to,
            price_from = price_from,
            price_to = price_to,
            max_stopovers = max_stopovers,
            adults = adults,
            children = children,
            dtime_to = dtime_to,
            atime_to = atime_to,
            ret_dtime_to = ret_dtime_to,
            ret_atime_to = ret_atime_to,
            selected_cabins = selected_cabins,
            max_fly_duration = max_fly_duration,
            limit = 100,
        )

        if (call.isSuccessful) {
            println(call)
            val flights_to_add = call.body()?.data
            val flights_filtered = flights_to_add
                ?.stream()
                ?.filter(Predicate { f -> f.availability.seats >= children + adults })
                ?.toList()
            if (flights_filtered != null) {
                if(flights_filtered.isNotEmpty()){
                    var setDestinations = mutableSetOf<String>()
                    val currency = call.body()!!.currency
                    for (f in flights_filtered) {
                        val call_image = repository.getCityImage(f.cityTo.lowercase())
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
                            n_passengers = children + adults
                        )
                        setDestinations.add(f.cityTo)
                        DataSource.flights.add(flight)
                        procesingFlights(flights = DataSource.flights, destinations = setDestinations)
                    }
                }
            } else {
                println("No se han encontrado vuelos")
            }
        } else {
            println("Ha ocurrido un error durante la llamada a la api")
        }
    }

    fun procesingFlights(flights: List<Flight>, destinations: Set<String>){
        DataSource.flightsListed.clear()
        for (d in destinations){
            DataSource.flightsListed[d] = flights
                .stream()
                .filter { f -> f.city_to == d }
                .collect(Collectors.toList())
        }
    }

    suspend fun getFlightRoutes(flight: Flight){
        for (r in flight!!.routes) {
            val location_departure_call = repository.getLocation(r.cityFrom.replace(",", " "))
            println(location_departure_call)
            val location_arrival_call = repository.getLocation(r.cityTo.replace(",", " "))
            println(location_arrival_call)
            if (location_departure_call.isSuccessful && location_arrival_call.isSuccessful) {
                print("LLamada exitosa a position")
                val locationDeparture = location_departure_call.body()?.data?.get(0)
                val locationArrival = location_arrival_call.body()?.data?.get(0)
                if (locationDeparture != null && locationArrival != null) {
                    DataSource.routesLocation[r.cityFrom] =
                        LatLng(locationDeparture.latitude, locationDeparture.longitude)
                    DataSource.routesLocation[r.cityTo] =
                        LatLng(locationArrival.latitude, locationArrival.longitude)
                }
            } else {
                println("\n\nFALLO EN EL SERVIDOR NO SE HAN ENCONTRADO LAS COORDENADAS\n\n")
            }
        }
        println(DataSource.routesLocation)
    }

}