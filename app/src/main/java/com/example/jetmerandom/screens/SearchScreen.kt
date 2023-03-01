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
import com.example.jetmerandom.SearchViewModel
import com.example.jetmerandom.screens.components.AutoCompleteSelect
import com.example.jetmerandom.screens.components.DatePickerCalendar


@Composable
fun SearchScreen(
    viewModel: SearchViewModel
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
            .padding(start = 25.dp, end = 25.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ){
        Spacer(modifier = Modifier.padding(50.dp))

        HeadOptions(
            viewModel = viewModel
        )

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
            ),
            viewModel = viewModel
        )
        
        Text(text = state.endDate.toString())

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
            onValueChange = {price = it},
        )



        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            DatePickerCalendar(
                label = "Start",
                minDate = LocalDate.now(),
                viewModel = viewModel
            )
            DatePickerCalendar(
                label = "End",
                minDate = LocalDate.now().plusDays(1),
                viewModel = viewModel
            )
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

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun HeadOptions(
    viewModel: SearchViewModel
){

    val state = viewModel.uiState.collectAsState().value

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
            if (state.isDirect){
                IconWithText(
                    stringResourceId = R.string.direct_flight,
                    iconResourceId = R.drawable.baseline_airplane_ticket_24)
            } else {
                IconWithText(
                    stringResourceId = R.string.indirect_flight,
                    iconResourceId = R.drawable.baseline_airplane_ticket_24)
            }
            Spacer(modifier = Modifier.width(70.dp))
            IconWithText(
                value = state.qAdults.toString(),
                iconResourceId = R.drawable.baseline_face_24)

            Spacer(modifier = Modifier.width(10.dp))
            IconWithText(
                value = state.qChilds.toString(),
                iconResourceId = R.drawable.baseline_child_care_24)
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
                    TextButton(onClick = { viewModel.setIsDirect(true) }) {
                        Text(
                            text = stringResource(id = R.string.direct_flight),
                            fontSize = 16.sp
                        )
                    }
                    TextButton(onClick = { viewModel.setIsDirect(false) }) {
                        Text(
                            text = stringResource(id = R.string.indirect_flight),
                            fontSize = 16.sp
                        )
                    }
                    Divider(color = Color.Gray, thickness = 1.dp)
                    PopUpRow(
                        viewModel = viewModel,
                        iconResourceId = R.drawable.baseline_face_24,
                        ageGroup = "Adult")
                    PopUpRow(
                        viewModel = viewModel,
                        iconResourceId = R.drawable.baseline_child_care_24,
                        ageGroup = "Child")
                }
            }
        }
    }

}

@Composable
fun PopUpRow(
    viewModel: SearchViewModel,
    @DrawableRes iconResourceId: Int,
    ageGroup: String
){

    val state = viewModel.uiState.collectAsState().value


    Row(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
    ) {
        IconButton(
            onClick = {
                if (ageGroup == "Adult" && state.qAdults > 0){
                    viewModel.setPassenger(state.qAdults.dec(), ageGroup)
                } else if (ageGroup == "Child" && state.qChilds > 0) {
                    viewModel.setPassenger(state.qChilds.dec(), ageGroup)
                }
            }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_remove_24),
                contentDescription = null
            )
        }
        Spacer(Modifier.width(20.dp))
        Icon(
            painter = painterResource(id = iconResourceId),
            contentDescription = null
        )

        if (ageGroup == "Adult"){
            Text(
                text = state.qAdults.toString(),
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(start = 5.dp)
            )
        } else {
            Text(
                text = state.qChilds.toString(),
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(start = 5.dp)
            )
        }
        Spacer(Modifier.width(20.dp))
        IconButton(
            onClick = {
                if (ageGroup == "Adult" && state.qAdults <= 10){
                    viewModel.setPassenger(state.qAdults.inc(), ageGroup)
                } else if (ageGroup == "Child" && state.qChilds <= 10) {
                    viewModel.setPassenger(state.qChilds.inc(), ageGroup)
                }
            }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_add_24),
                contentDescription = null
            )
        }
    }
}

@Composable
fun IconWithText(
    @StringRes stringResourceId: Int,
    @DrawableRes iconResourceId: Int,
){
    Icon(
        painter = painterResource(id = iconResourceId),
        contentDescription = null
    )
    Text(
        text = stringResource(id = stringResourceId),
    )
}
@Composable
fun IconWithText(
    value: String,
    @DrawableRes iconResourceId: Int,
){
    Icon(
        painter = painterResource(id = iconResourceId),
        contentDescription = null
    )
    Text(
        text = value,
    )
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
    SearchScreen(
        viewModel = SearchViewModel()
        )
}



