package com.example.jetmerandom

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.jetmerandom.data.DataSource
import com.example.jetmerandom.data.SearchUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate

class SearchViewModel: ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()




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