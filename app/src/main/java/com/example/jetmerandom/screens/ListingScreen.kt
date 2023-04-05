package com.example.jetmerandom

import android.telecom.Call.Details
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.jetmerandom.data.DataSource.flights
import com.example.jetmerandom.data.Flight
import com.example.jetmerandom.screens.components.CardRoute

@Composable
fun ListinScreen(
    viewModel: SearchViewModel,
    onDetailsClicked: () -> Unit = {}
){
    println(flights)
    LazyColumn(modifier = Modifier.background(MaterialTheme.colors.background)) {
        items(flights){
            FlightItem(flight = it,
                onDetailsClicked = onDetailsClicked,
                viewModel = viewModel
            )
        }
    }
}

@Composable
fun FlightItem(flight: Flight,onDetailsClicked: () -> Unit = {}, viewModel: SearchViewModel){
    var expanded by remember { mutableStateOf(false) }

    val color by animateColorAsState(
        targetValue = if (expanded) Color(0xFF97ABE6) else MaterialTheme.colors.surface,
    )

    val departure: String = flight.routes[0].utc_departure.split("T")[0]
    val arrival: String =flight.routes.last().utc_arrival.split("T")[0]

    Card(
        elevation = 4.dp,
        modifier = Modifier.padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow,
                    )
                )
                .background(color = color)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                CityIcon(flight.imageURL)
                CardInfo(flight.city_to,
                    flight.price,
                    flight.currency,
                    "$departure $arrival"
                )
                Spacer(Modifier.weight(1f))
                ExpandedButton(
                    expanded = expanded,
                    onClick = { expanded = !expanded }
                )

            }
            if (expanded) {
                CardRoute(flight = flight)
                Button(onClick = {
                    viewModel.setFligthDetails(flight)
                    viewModel.getFlightRoute()
                    onDetailsClicked()
                },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text(text = "Details")
                }
                Button(onClick = {
                    viewModel.setFligthDetails(flight)
                    viewModel.getFlightRoute()
                },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text(text = "Prueba")
                }
            }
        }
    }
}


@Composable
fun ExpandedButton(expanded: Boolean, onClick: ()-> Unit){
    IconButton(onClick = onClick) {
        Icon(
            imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.ArrowDropDown,
            tint = MaterialTheme.colors.secondary,
            contentDescription = stringResource(R.string.expand_card),
        )
    }
}

@Composable
fun CardInfo(cityTo:String,
             price:Int,
             currency:String,
             date:String
){
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text(
                text = cityTo,
                style = MaterialTheme.typography.h5,
                modifier = Modifier.width(150.dp)
            )
            Spacer(modifier = Modifier.padding(start = 65.dp))
            Text(
                text = "$price $currency",
                style = MaterialTheme.typography.subtitle2
            )

        }

        Text(
            text = date,
            style = MaterialTheme.typography.body2,
            fontSize = 14.sp
        )
    }
}

@Composable
fun CityIcon(cityImage:String) {
    Image(
        modifier = Modifier
            .size(68.dp)
            .padding(8.dp)
            .clip(RoundedCornerShape(35)),
        contentScale = ContentScale.Crop,
        painter = rememberAsyncImagePainter(model = cityImage),
        contentDescription = null
    )
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun ListingPreview(){
    ListinScreen(
        viewModel = SearchViewModel()
    )
}