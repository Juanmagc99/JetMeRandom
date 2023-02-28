package com.example.jetmerandom

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

    fun onSearchClicked(startDate: LocalDate,
                        endDate: LocalDate,
                        priceRange: ClosedFloatingPointRange<Float>,
                        maxTime: Int,
                        origin: String,
                        isDirect: Boolean
    ){
        _uiState.update { currentState ->
            currentState.copy(
                startDate = startDate,
                endDate = endDate,
                minPrice = priceRange.start,
                maxPrice = priceRange.endInclusive,
                maxTime = maxTime,
                origin = origin,
                isDirect = isDirect
            )
        }
    }

}