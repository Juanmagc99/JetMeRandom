package com.example.jetmerandom

import androidx.lifecycle.ViewModel
import com.example.jetmerandom.data.SearchUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SearchViewModel: ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()


    /*fun showStartDataPicker(showDatePicker: Boolean){
        _uiState.update { currentState ->
            currentState.copy(
                startPicker = showDatePicker,
            )
        }
    }*/

}