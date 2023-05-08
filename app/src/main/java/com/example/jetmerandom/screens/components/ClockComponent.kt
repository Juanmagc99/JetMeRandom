package com.example.jetmerandom.screens.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.jetmerandom.SearchViewModel
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.clock.ClockDialog
import com.maxkeppeler.sheets.clock.models.ClockConfig
import com.maxkeppeler.sheets.clock.models.ClockSelection
import java.time.LocalTime

@Composable
fun ClockTime(viewModel: SearchViewModel, flag:String, ret:Boolean){
    val selectedTime = remember { mutableStateOf(LocalTime.of(23, 59, 0)) }
    val clockState = rememberSheetState()
    OutlinedButton(
        onClick = {
            clockState.show()
        },
        contentPadding = PaddingValues(
            start = 20.dp,
            top = 12.dp,
            end = 20.dp,
            bottom = 12.dp
        ),
        modifier = Modifier
            .size(141.dp, 60.dp),
        border = ButtonDefaults.outlinedBorder,
    ){
        if(!ret){
            if (flag == "G"){
                Text(text = "Departure\n" + viewModel.uiState.collectAsState().value.depTime.toString(),
                    textAlign = TextAlign.Center
                )
            } else {
                Text(text = "Arrival\n" + viewModel.uiState.collectAsState().value.arrTime.toString(),
                    textAlign = TextAlign.Center
                )
            }
        } else {
            if (flag == "G"){
                Text(
                    text = "Ret Departure\n" + viewModel.uiState.collectAsState().value.rdepTime.toString(),
                    textAlign = TextAlign.Center
                    )
            } else {
                Text(
                    text = "Ret Arrival \n" + viewModel.uiState.collectAsState().value.rarrTime.toString(),
                    textAlign = TextAlign.Center
                )
            }
        }

    }

    ClockDialog(
        state = clockState,
        selection = ClockSelection.HoursMinutes { hours, minutes ->
            selectedTime.value = LocalTime.of(hours, minutes, 0)
            if(!ret){
                if (flag == "G"){
                    viewModel.setDepTime(selectedTime.value)
                } else {
                    viewModel.setArrTime(selectedTime.value)
                }
            } else {
                if (flag == "G"){
                    viewModel.setrDepTime(selectedTime.value)
                } else {
                    viewModel.setrArrTime(selectedTime.value)
                }
            }

        },
        config = ClockConfig(
            defaultTime = selectedTime.value,
            is24HourFormat = true
        )
    )
}