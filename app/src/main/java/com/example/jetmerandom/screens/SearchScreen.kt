package com.example.jetmerandom.screens

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.window.SecureFlagPolicy
import com.example.jetmerandom.screens.components.AutoCompleteSelect


@Composable
fun SearchScreen(
    onSearchClicked: () -> Unit,
){

    var amountOfHours by remember {
        mutableStateOf("")
    }

    var startDate by remember {
        mutableStateOf(LocalDate.now())
    }

    var endDate by remember {
        mutableStateOf(LocalDate.now().plusDays(4))
    }

    val focusManager = LocalFocusManager.current

    var price by remember { mutableStateOf(80.0f..300.0f) }


    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 25.dp, end = 25.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ){
        Spacer(modifier = Modifier.padding(50.dp))

        HeadOptions()

        AutoCompleteSelect(
            "Cities",
            cities,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions (
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            )
        )


        EditNumberField(
            modifier = Modifier
                .width(130.dp),
            value = amountOfHours,
            onValueChange = {amountOfHours = it},
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions (
                onDone = {
                    focusManager.clearFocus()
                }
            ),
            labelResourceId = R.string.hours_label,
            iconResourceId = R.drawable.baseline_access_time_24
        )

        PriceRange(
            price = price,
            onValueChange = {price = it}
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            DatePickerCalendar(
                label = "Start",
                date = startDate,
                onValueChange = { startDate = it },
                minDate = startDate
            )
            DatePickerCalendar(
                label = "End",
                date = endDate,
                onValueChange = { endDate = it },
                minDate = startDate
            )
        }


    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PriceRange(
    price: ClosedFloatingPointRange<Float>,
    onValueChange: (ClosedFloatingPointRange<Float>) -> Unit
){
    val range = 10.0f..500.0f

    Column() {
        RangeSlider(
            value = price,
            valueRange = range,
            onValueChange =  onValueChange ,
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

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun HeadOptions(){

    var popupControl by remember { mutableStateOf(false) }

    var nAdults by remember { mutableStateOf(2) }

    var nChilds by remember { mutableStateOf(0) }

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
            onClick = { popupControl = true },
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
            Text(text = nAdults.toString())
            Spacer(modifier = Modifier.width(10.dp))
            Icon(
                painter = painterResource(id = R.drawable.baseline_child_care_24),
                contentDescription = null
            )
            Text(text = nChilds.toString())
        }
    }

    if (popupControl) {
        Popup (
            onDismissRequest = {popupControl = false},
            alignment = Alignment.BottomStart,
            properties = popupProperties,
        ){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .background(Color.LightGray, RoundedCornerShape(16.dp))
            ){
                IconButton(
                    onClick = { popupControl = false },
                    modifier = Modifier
                        .align(Alignment.TopEnd)

                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_cancel_24),
                        contentDescription = null,
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp)
                        .align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Spacer(Modifier.height(30.dp))
                    TextButton(onClick = { /*TODO*/ }) {
                        Text(
                            text = "Direct flight",
                            fontSize = 16.sp
                        )
                    }
                    TextButton(onClick = { /*TODO*/ }) {
                        Text(
                            text = "Indirect flight",
                            fontSize = 16.sp
                        )
                    }
                    Divider(color = Color.Gray, thickness = 1.dp)
                    Row(
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                    ) {
                        IconButton(
                            onClick = { /*TODO*/ }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_add_24),
                                contentDescription = null
                            )
                        }
                        Spacer(Modifier.width(20.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_face_24),
                            contentDescription = null
                        )
                        Text(
                            text = nAdults.toString(),
                            fontSize = 18.sp,
                            modifier = Modifier
                                .padding(start = 5.dp)
                        )
                        Spacer(Modifier.width(20.dp))
                        IconButton(
                            onClick = { /*TODO*/ }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_remove_24),
                                contentDescription = null
                            )
                        }
                    }
                    Row(
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                    ) {
                        IconButton(
                            onClick = { /*TODO*/ }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_add_24),
                                contentDescription = null
                            )
                        }
                        Spacer(Modifier.width(20.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_child_care_24),
                            contentDescription = null
                        )
                        Text(
                            text = nChilds.toString(),
                            fontSize = 18.sp,
                            modifier = Modifier
                                .padding(start = 5.dp)
                        )
                        Spacer(Modifier.width(20.dp))
                        IconButton(
                            onClick = { /*TODO*/ }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_remove_24),
                                contentDescription = null
                            )
                        }
                    }
                }
            }
        }
    }

}


@Composable
fun DatePickerCalendar(
    label: String,
    date: LocalDate,
    onValueChange: (LocalDate) -> Unit,
    minDate: LocalDate
){
    var showPicker by remember {
        mutableStateOf(false)
    }

    var stringDate by remember {
        mutableStateOf("")
    }

    Column() {
        Text(
            text = date.toString(),
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
    keyboardActions: KeyboardActions,
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
        keyboardActions = keyboardActions,
        modifier = modifier,
        maxLines = 1,
    )

}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewSearch(){
    SearchScreen {

    }
}