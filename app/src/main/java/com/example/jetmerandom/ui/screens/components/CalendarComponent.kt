package com.example.jetmerandom.screens.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.jetmerandom.ui.SearchViewModel
import com.squaredem.composecalendar.ComposeCalendar
import java.time.LocalDate


@Composable
fun DatePickerCalendar(
    label: String,
    minDate: LocalDate,
    viewModel: SearchViewModel
){
    var showPicker by remember {
        mutableStateOf(false)
    }

    var stringDate by remember {
        mutableStateOf("")
    }

    Column() {
        Text(
            text = stringDate,
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            color = Color.DarkGray
        )

        OutlinedButton(
            onClick = {
                showPicker = true
            },
            contentPadding = PaddingValues(
                start = 20.dp,
                top = 12.dp,
                end = 20.dp,
                bottom = 12.dp
            ),
            modifier = Modifier.size(110.dp, 50.dp),
            border = ButtonDefaults.outlinedBorder
        ){
            Icon(
                Icons.Filled.DateRange,
                contentDescription = "Date picker icon",

                )
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text(text = label)
        }

        if (showPicker) {
            ComposeCalendar(
                startDate = minDate,
                minDate = minDate,
                onDone = { it: LocalDate ->
                    showPicker = false
                    stringDate = it.toString()
                    viewModel.setDate(it, label)
                },
                onDismiss = {
                    showPicker = false
                },
            )
        }
    }

}