package com.example.jetmerandom.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetmerandom.data.FlightLikedUiState
import com.example.jetmerandom.data.database.entities.FlightEntity
import com.example.jetmerandom.data.flight.Flight
import com.example.jetmerandom.domain.FlightsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LikedFlightViewModel @Inject constructor(
    private val flightsUseCases: FlightsUseCases
): ViewModel(){

    private val _uiState = MutableStateFlow(FlightLikedUiState())
    val uiState: StateFlow<FlightLikedUiState> = _uiState.asStateFlow()

    fun getAllFlights(){
        viewModelScope.launch {
            _uiState.update {
                it.copy(flightsLiked = flightsUseCases.getLikedFlights())
            }
        }
    }

    fun onLikedFlight(flight: Flight){
        viewModelScope.launch {
            flightsUseCases.insertFlight(flight)
        }
    }

    fun onDeleteFlight(flightEntity: FlightEntity){
        viewModelScope.launch {
            flightsUseCases.deleteFlight(flightEntity)
            getAllFlights()
        }
    }

}