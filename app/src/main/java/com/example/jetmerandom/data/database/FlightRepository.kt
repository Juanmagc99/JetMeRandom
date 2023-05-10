package com.example.jetmerandom.data.database

import com.example.jetmerandom.data.database.dao.FlightDao
import com.example.jetmerandom.data.database.entities.FlightEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FlightRepository @Inject constructor(
    private val flightDao: FlightDao
) {

    suspend fun getAllFlightsFromDB():List<FlightEntity>{
        return flightDao.getAll()
    }

    suspend fun deleteFlightFromDB(flightEntity: FlightEntity){
        flightDao.delete(flightEntity)
    }

    suspend fun insertFlight(flightEntity: FlightEntity){
        flightDao.insert(flightEntity)
    }

}