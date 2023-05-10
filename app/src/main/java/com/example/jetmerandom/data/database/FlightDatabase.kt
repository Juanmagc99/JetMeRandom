package com.example.jetmerandom.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.jetmerandom.data.database.dao.FlightDao
import com.example.jetmerandom.data.database.entities.FlightEntity

@Database(entities = [FlightEntity::class], version = 1)
abstract class FlightDatabase: RoomDatabase() {

    abstract fun getFlightDao():FlightDao
}