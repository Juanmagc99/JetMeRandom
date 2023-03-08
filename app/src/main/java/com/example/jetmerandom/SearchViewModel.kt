package com.example.jetmerandom

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.jetmerandom.data.SearchUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate

class SearchViewModel: ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()


    fun onSearchClicked(
                        priceRange: ClosedFloatingPointRange<Float>,
                        maxTime: Int,
                        isDirect: Boolean
    ){
        _uiState.update { currentState ->
            currentState.copy(
                minPrice = priceRange.start,
                maxPrice = priceRange.endInclusive,
                maxTime = maxTime,
                isDirect = isDirect
            )
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

    fun checkDates(){
        val currentState = _uiState.value
        val start = uiState.value.startDate
        val end = uiState.value.endDate

        if (start.isBefore(end)) {
            _uiState.value = currentState.copy(checkDates = true)
        } else {
            _uiState.value = currentState.copy(checkDates = false)
        }
    }

    fun setPriceRange(priceRange: ClosedFloatingPointRange<Float>){
        val currentState = _uiState.value
        val minPrice = priceRange.start
        val maxPrice = priceRange.endInclusive
        _uiState.value = currentState.copy(minPrice = minPrice)
        _uiState.value = currentState.copy(maxPrice = maxPrice)
    }

    fun onOriginSelected(origin: String) {
        val currentState = _uiState.value
        _uiState.value = currentState.copy(origin = origin)
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
    }

}