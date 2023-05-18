package com.example.jetmerandom.data.database

import com.example.jetmerandom.BuildConfig
import com.example.jetmerandom.data.database.dao.FlightDao
import com.example.jetmerandom.data.database.entities.FlightEntity
import com.example.jetmerandom.data.flight.FlightsResponse
import com.example.jetmerandom.data.image.ImageResponse
import com.example.jetmerandom.data.network.APIClients
import com.example.jetmerandom.data.position.LocationResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class FlightRepository @Inject constructor(
    private val flightDao: FlightDao,
    private val apiClients: APIClients
) {

    val kiwi_api = BuildConfig.KIWI_API_KEY
    val location_api = BuildConfig.LOCATION_API_KEY


    suspend fun getAllFlightsFromDB(): List<FlightEntity> {
        return flightDao.getAll()
    }

    suspend fun deleteFlightFromDB(flightEntity: FlightEntity) {
        flightDao.delete(flightEntity)
    }

    suspend fun insertFlight(flightEntity: FlightEntity) {
        flightDao.insert(flightEntity)
    }

    suspend fun getFlightsFromNet(
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
    ): Response<FlightsResponse> {
        return apiClients.getFlights(
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
            apiKey = kiwi_api
        )
    }
    suspend fun getCityImage(city_name : String) : Response<ImageResponse> {
        return apiClients.getImage(city_name)
    }

    suspend fun getLocation(query: String) : Response<LocationResponse> {
        return apiClients.getCitiesLocation(city_name = query, location_api = location_api)
    }
}