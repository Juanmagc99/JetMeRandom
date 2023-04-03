package com.example.jetmerandom.screens

import HeadOptions
import android.widget.Toast
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jetmerandom.R
import com.example.jetmerandom.data.DataSource.cities

import java.time.LocalDate
import kotlin.math.roundToInt
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import com.example.jetmerandom.SearchViewModel
import com.example.jetmerandom.screens.components.AutoCompleteSelect
import com.example.jetmerandom.screens.components.DatePickerCalendar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onNextButtonClicked: () -> Unit = {},
){

    val state = viewModel.uiState.collectAsState().value

    val focusManager = LocalFocusManager.current

    var amountOfHours by remember {
        mutableStateOf("")
    }

    var price by remember { mutableStateOf(80.0f..300.0f) }



    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 25.dp, start = 25.dp, end = 25.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ){

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
            keyboardActions = KeyboardActions (
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),
            viewModel = viewModel
        )


        EditNumberField(
            modifier = Modifier
                .width(145.dp),
            value = amountOfHours,
            onValueChange = {amountOfHours = it},
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions (
                onDone = {
                    focusManager.clearFocus()
                    viewModel.setMaxHours(amountOfHours.toInt())
                }
            ),
            labelResourceId = R.string.hours_label,
            iconResourceId = R.drawable.baseline_access_time_24,
            viewModel = viewModel
        )

        Spacer(modifier = Modifier.padding(5.dp))

        PriceRange(
            price = price,
            onValueChange = {price = it},
        )
        
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                DatePickerCalendar(
                    label = "Start",
                    minDate = LocalDate.now(),
                    viewModel = viewModel
                )
                DatePickerCalendar(
                    label = "End",
                    minDate = state.startDate,
                    viewModel = viewModel
                )
            }

            if(!state.checkDates){
                Text(
                    text = stringResource(R.string.date_error_message),
                    color = Color.Red,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.padding(5.dp))

        Button(
            onClick = {
                    viewModel.getFlights()
                    runBlocking {
                        delay(2000)
                    }

                    onNextButtonClicked()

            },
        ) {
            Text(text = "Search", fontSize = 22.sp)
            Icon(painter = painterResource(id = R.drawable.baseline_navigate_next_24),
                contentDescription = null)
        }

    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PriceRange(
    price: ClosedFloatingPointRange<Float>,
    onValueChange: (ClosedFloatingPointRange<Float>) -> Unit,
){
    val range = 10.0f..500.0f

    Column() {
        RangeSlider(
            value = price,
            valueRange = range,
            onValueChange =  onValueChange,
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

       if (!viewModel.uiState.collectAsState().value.checkHours){
           Text(
               text = "Cant input a negative nº of hours",
               color = Color.Red,
               fontSize = 12.sp
           )
       }
    }


}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewSearch(){
    SearchScreen(
        viewModel = SearchViewModel()
        )
}



