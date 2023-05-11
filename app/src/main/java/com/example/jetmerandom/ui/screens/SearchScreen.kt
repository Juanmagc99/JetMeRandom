package com.example.jetmerandom.screens

import HeadOptions
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.jetmerandom.R
import com.example.jetmerandom.data.DataSource.cities

import kotlin.math.roundToInt
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import com.example.jetmerandom.ui.SearchViewModel
import com.example.jetmerandom.screens.components.AutoCompleteSelect
import com.example.jetmerandom.screens.components.ClockTime
import com.example.jetmerandom.screens.components.OptionsPick
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle


@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onNextButtonClicked: () -> Unit = {},
){

    val mContext = LocalContext.current

    val state = viewModel.uiState.collectAsState().value

    val focusManager = LocalFocusManager.current

    var minNights by remember {
        mutableStateOf("")
    }

    var maxHours by remember {
        mutableStateOf("")
    }

    var price by remember { mutableStateOf(80.0f..300.0f) }

    val scrollState = rememberScrollState()

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, start = 25.dp, end = 20.dp)
            .verticalScroll(
                state = scrollState,
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Spacer(modifier = Modifier.padding(10.dp))

        HeadOptions(
            viewModel = viewModel
        )

        Spacer(modifier = Modifier.padding(5.dp))

        AutoCompleteSelect(
            "From",
            cities,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),
            viewModel = viewModel
        )


        EditNumberField(
            modifier = Modifier
                .width(145.dp),
            value = minNights,
            onValueChange = {
                minNights = it
                if (it.isNotBlank() && !it.contains("-")
                    && !it.contains("+") && it.toIntOrNull() != null) {
                    viewModel.setMinNights(it.toInt())
                } else {
                    viewModel.setMinNights(-1)
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next,
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.clearFocus()
                    if (minNights.isNotBlank() && !minNights.contains("-")
                        && minNights.contains("+") && minNights.toIntOrNull() != null) {
                        viewModel.setMaxHours(minNights.toInt())
                    } else {
                        viewModel.setMaxHours(-1)
                    }
                }
            ),
            labelResourceId = R.string.nights_label,
            iconResourceId = R.drawable.baseline_access_time_24,
            viewModel = viewModel
        )

        if(!viewModel.uiState.collectAsState().value.checkDays){
            Text(
                text = "You must input positive integer only",
                color = Color.Red,
                fontSize = 12.sp
            )
        }

        EditNumberField(
            modifier = Modifier
                .width(145.dp),
            value = maxHours,
            onValueChange = {
                maxHours = it
                if (it.isNotBlank() && !it.contains("-")
                    && !it.contains("+") && it.toIntOrNull() != null) {
                    viewModel.setMaxHours(it.toInt())
                } else {
                    viewModel.setMaxHours(-1)
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    if (maxHours.isNotBlank() && !maxHours.contains("-")
                        && !maxHours.contains("+") && maxHours.toIntOrNull() != null) {
                        viewModel.setMaxHours(maxHours.toInt())
                    } else {
                        viewModel.setMaxHours(-1)
                    }
                }
            ),
            labelResourceId = R.string.hours_label,
            iconResourceId = R.drawable.baseline_access_time_24,
            viewModel = viewModel
        )

        if(!viewModel.uiState.collectAsState().value.checkHours){
            Text(
                text = "You must input positive integer only",
                color = Color.Red,
                fontSize = 12.sp
            )
        }


        Spacer(modifier = Modifier.padding(5.dp))

        PriceRange(
            price = price,
            onValueChange = { price = it },
            viewModel
        )


        val calendarState = rememberSheetState()



        Text(text = state.startDate.toString() + " / " + state.endDate.toString())

        OutlinedButton(
            onClick = {
                calendarState.show()
            },
            contentPadding = PaddingValues(
                start = 20.dp,
                top = 12.dp,
                end = 20.dp,
                bottom = 12.dp
            ),
            modifier = Modifier.size(130.dp, 50.dp),
            border = ButtonDefaults.outlinedBorder
        ) {
            Icon(
                Icons.Filled.DateRange,
                contentDescription = "Date picker icon",

                )
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text(text = stringResource(R.string.select_dates))
        }

        CalendarDialog(
            state = calendarState,
            config = CalendarConfig(
                monthSelection = true,
                style = CalendarStyle.MONTH,
            ),
            selection = CalendarSelection.Period { d1, d2 ->
                viewModel.setDate(d1, "Start")
                viewModel.setDate(d2, "End")
            },
        )


        if (!state.checkDates) {
            Text(
                text = stringResource(R.string.date_error_message),
                color = Color.Red,
                fontSize = 12.sp
            )
        }


        Row() {
            ClockTime(viewModel = viewModel, flag = "G", false)
            Spacer(modifier = Modifier.width(16.dp))
            ClockTime(viewModel = viewModel, flag = "B", false)
        }

        Row() {
            ClockTime(viewModel = viewModel, flag = "G", true)
            Spacer(modifier = Modifier.width(16.dp))
            ClockTime(viewModel = viewModel, flag = "B", true)
        }



        OptionsPick(viewModel)

        Button(
            onClick = {
                viewModel.getFlights(onNextButtonClicked, mContext = mContext)
                println(state)
            },
        ) {
            Text(text = "Search", fontSize = 22.sp)
            Icon(
                painter = painterResource(id = R.drawable.baseline_navigate_next_24),
                contentDescription = null
            )
        }
    }

}



@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PriceRange(
    price: ClosedFloatingPointRange<Float>,
    onValueChange: (ClosedFloatingPointRange<Float>) -> Unit,
    viewModel: SearchViewModel
){
    val range = 10.0f..1500.0f

    Column() {
        RangeSlider(
            value = price,
            valueRange = range,
            onValueChange =  {
                onValueChange(it)
                viewModel.setPriceRange(price.start, price.endInclusive)
                             },
            steps = 15
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
fun EditNumberField(
    @StringRes labelResourceId: Int,
    modifier: Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
    viewModel: SearchViewModel,
    @DrawableRes iconResourceId: Int,
){

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
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


}




