package com.example.jetmerandom.data.database.dao

import androidx.room.*
import com.example.jetmerandom.data.database.entities.FlightEntity
import com.example.jetmerandom.data.flight.Flight
import kotlinx.coroutines.flow.Flow

@Dao
interface FlightDao {

    @Query("SELECT * FROM flight_table")
    suspend fun getAll():List<FlightEntity>

    @Delete
    suspend fun delete(flightEntity: FlightEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(flightEntity: FlightEntity)

}