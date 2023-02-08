package com.example.jetmerandom.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jetmerandom.R
import com.example.jetmerandom.data.DataSource.cities
import com.squaredem.composecalendar.ComposeCalendar
import java.time.LocalDate
import kotlin.math.round


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SearchScreen(

){

    var showStartPicker by remember {
        mutableStateOf(false)
    }

    Column (
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ){
        Spacer(modifier = Modifier.padding(50.dp))
        AutoCompleteSelect("Cities", cities)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            DatePickerCalendar(label = "Start")
            DatePickerCalendar(label = "End")
        }
    }
}

@Composable
fun DatePickerCalendar(
    label: String
){
    var showPicker by remember {
        mutableStateOf(false)
    }

    Button(
        onClick = {
            showPicker = true
        },
        contentPadding = PaddingValues(
            start = 20.dp,
            top = 12.dp,
            end = 20.dp,
            bottom = 12.dp
        ),
        modifier = Modifier.size(110.dp, 50.dp)
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
            minDate = LocalDate.now(),
            onDone = { it: LocalDate ->
                showPicker = false
                println(it)
            },
            onDismiss = {
                showPicker = false
            }
        )
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AutoCompleteSelect(label: String, options: List<String>){

    var selectedItem by remember {
        mutableStateOf("")
    }

    var expanded by remember {
        mutableStateOf(false)
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded}
    ) {
        OutlinedTextField(
            value = selectedItem,
            onValueChange = { selectedItem = it },
            label = {
                Text(text = label)
                    },
            trailingIcon = {
                if (!expanded){
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_search_24),
                        contentDescription = "arrow up icon")
                }else {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_cancel_24),
                        contentDescription = "search icon")
                }
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )

        val optionsFiltered =
            options.filter { it.contains(selectedItem, ignoreCase = true) }

        if (optionsFiltered.isNotEmpty()) {
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                optionsFiltered.forEach { selectionOption ->
                    DropdownMenuItem(
                        onClick = {
                            selectedItem = selectionOption
                            expanded = false
                        }
                    ) {
                        Text(text = selectionOption)
                    }
                }
            }
        }
    }

}
