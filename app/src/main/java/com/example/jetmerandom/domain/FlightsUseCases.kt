package com.example.jetmerandom.domain

import com.example.jetmerandom.data.database.FlightRepository
import com.example.jetmerandom.data.database.entities.FlightEntity
import com.example.jetmerandom.data.flight.Flight
import com.example.jetmerandom.data.flight.toEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FlightsUseCases @Inject constructor(
    private val repository: FlightRepository
){
    suspend fun getLikedFlights():List<FlightEntity>{
        return repository.getAllFlightsFromDB()
    }

    suspend fun insertFlight(flight: Flight){
        return repository.insertFlight(flight.toEntity())
    }

    suspend fun deleteFlight(flightEntity: FlightEntity){
        return repository.deleteFlightFromDB(flightEntity)
    }

}