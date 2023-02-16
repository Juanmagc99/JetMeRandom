package com.example.jetmerandom.screens

import androidx.annotation.DrawableRes
import androidx.annotation.FloatRange
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jetmerandom.R
import com.example.jetmerandom.data.DataSource.cities

import com.squaredem.composecalendar.ComposeCalendar
import java.time.LocalDate
import kotlin.math.roundToInt
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.window.SecureFlagPolicy


@Composable
fun SearchScreen(
    onStartDatePicked: (LocalDate) -> Unit = {},
    onEndDatePicked: (LocalDate) -> Unit = {},
){

    var amountOfHours by remember {
        mutableStateOf("")
    }

    val focusManager = LocalFocusManager.current


    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 25.dp, end = 25.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ){
        Spacer(modifier = Modifier.padding(50.dp))

        HeadOptions()

        AutoCompleteSelect("Cities", cities)

        EditNumberField(
            modifier = Modifier
                .width(130.dp),
            value = amountOfHours,
            onValueChange = {amountOfHours = it},
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next,
            ),
            labelResourceId = R.string.hours_label,
            iconResourceId = R.drawable.baseline_access_time_24
        )

        PriceRange()

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            DatePickerCalendar(label = "Start", onDatePicked = onStartDatePicked)
            DatePickerCalendar(label = "End", onDatePicked = onEndDatePicked)
        }


    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun HeadOptions(){

    var popupControl by remember { mutableStateOf(false) }

   val popupProperties = PopupProperties(
        dismissOnBackPress = true,
        dismissOnClickOutside = true,
        securePolicy = SecureFlagPolicy.SecureOff,
        clippingEnabled = true,
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(width = 2.dp, color = Color.Blue),

    ) {
        TextButton(
            onClick = { popupControl = !popupControl },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_airplane_ticket_24),
                contentDescription = null
            )
            Text(
                text = "Direct flight",
            )
            Spacer(modifier = Modifier.width(70.dp))
            Icon(
                painter = painterResource(id = R.drawable.baseline_face_24),
                contentDescription = null
            )
            Text(
                text = "2",

            )
            Spacer(modifier = Modifier.width(10.dp))
            Icon(
                painter = painterResource(id = R.drawable.baseline_child_care_24),
                contentDescription = null
            )
            Text(
                text = "0",

                )
        }
    }

    if (popupControl) {
        Popup (
            onDismissRequest = {popupControl = false},
            alignment = Alignment.BottomCenter,
            properties = popupProperties,
        ){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp)
                    .background(Color.LightGray, RoundedCornerShape(16.dp))
            ){
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    TextButton(onClick = { /*TODO*/ }) {
                        Text(text = "Direct flight")
                    }
                    TextButton(onClick = { /*TODO*/ }) {
                        Text(text = "Indirect flight")
                    }
                    TextButton(onClick = { /*TODO*/ }) {
                        Text(text = "Hola")
                    }
                    TextButton(onClick = { /*TODO*/ }) {
                        Text(text = "Hola")
                    }
                }
            }
        }
    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PriceRange(

){
    val range = 10.0f..500.0f
    var price by remember { mutableStateOf(80.0f..300.0f) }



    Column() {
        RangeSlider(
            value = price,
            valueRange = range,
            onValueChange = { price = it },
            onValueChangeFinished = {/*TODO*/}
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "$" + price.start.roundToInt().toString(),
            )
            Text(
                text = "$" + price.endInclusive.roundToInt().toString(),
            )
        }
    }



}

@Composable
fun DatePickerCalendar(
    label: String,
    onDatePicked: (LocalDate) -> Unit,
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
            color = Color.Gray
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
                minDate = LocalDate.now(),
                onDone = { it: LocalDate ->
                    showPicker = false
                    onDatePicked(it)
                    stringDate = it.toString()
                },
                onDismiss = {
                    showPicker = false
                },
            )
        }
    }

}

@Composable
fun EditNumberField(
    @StringRes labelResourceId: Int,
    modifier: Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions,
    @DrawableRes iconResourceId: Int,
){
    TextField(
        label = {
            Text(text = stringResource(id = labelResourceId))
                },
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        keyboardOptions = keyboardOptions,
        leadingIcon = {
            Icon(
                painter = painterResource(id = iconResourceId),
                contentDescription = null
            )
        },
        modifier = modifier,
        maxLines = 1,
    )

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
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            maxLines = 1
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

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewSearch(){
    SearchScreen(
    )
}