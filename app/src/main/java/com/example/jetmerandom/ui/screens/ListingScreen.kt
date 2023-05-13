package com.example.jetmerandom

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.jetmerandom.data.DataSource.flightsListed
import com.example.jetmerandom.data.DataSource.flightsToCompare
import com.example.jetmerandom.data.flight.Flight
import com.example.jetmerandom.screens.components.CardRoute
import com.example.jetmerandom.ui.SearchViewModel
import java.time.Duration

@Composable
fun ListinScreen(
    viewModel: SearchViewModel,
    onDetailsClicked: () -> Unit = {}
) {

    LazyColumn(
        modifier = Modifier
            .background(MaterialTheme.colors.background)
    ) {

        for (flights in flightsListed) {
            var flightsToDisplay: List<Flight>
            item {
                Text(
                    text = flights.key,
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.padding(8.dp),
                    fontWeight = FontWeight.Black
                )
            }
            if (flights.value.size >= 5) {
                flightsToDisplay = flights.value.subList(0, 4)
            } else {
                flightsToDisplay = flights.value
            }
            items(flightsToDisplay) {
                FlightItem(
                    flight = it,
                    onDetailsClicked = onDetailsClicked,
                    viewModel = viewModel
                )
            }
        }
    }


}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FlightItem(flight: Flight, onDetailsClicked: () -> Unit = {}, viewModel: SearchViewModel) {
    var expanded by remember { mutableStateOf(false) }

    val color by animateColorAsState(
        targetValue = if (expanded) Color(0xFFAFBEEB) else MaterialTheme.colors.surface,
    )

    var selected by remember {
        mutableStateOf(false)
    }

    val departure: String = flight.routes[0].utc_departure.split("T")[0]
    val arrival: String = flight.routes.last().utc_arrival.split("T")[0]

    selected = flightsToCompare.contains(flight)

    Card(
        elevation = 6.dp,
        modifier = Modifier
            .padding(8.dp)
            .combinedClickable(
                onClick = {

                },
                onLongClick = {
                    viewModel.setToCompare(flight)
                    selected = !selected
                },
            ),
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
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CityIcon(flight.imageURL)
                CardInfo(
                    flight.price,
                    flight.currency,
                    departure,
                    arrival,
                    flight.duration
                )
                Spacer(Modifier.weight(1f))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    if (selected) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_check_circle_24),
                            contentDescription = "check to compare"
                        )
                    }
                    ExpandedButton(
                        expanded = expanded,
                        onClick = { expanded = !expanded }
                    )
                }


            }
            if (expanded) {
                CardRoute(flight = flight)
                Button(
                    onClick = {
                        viewModel.setFligthDetails(flight)
                        viewModel.getFlightRoute(onDetailsClicked)
                    },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text(text = "Details")
                }
            }
        }
    }
}


@Composable
fun ExpandedButton(expanded: Boolean, onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.ArrowDropDown,
            tint = MaterialTheme.colors.secondary,
            contentDescription = stringResource(R.string.expand_card),
        )
    }
}

@Composable
fun CardInfo(
    price: Int,
    currency: String,
    dateFrom: String,
    dateTo: String,
    duration: com.example.jetmerandom.data.flight.Duration
) {

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        CarColumn(text1 = dateFrom, text2 = dateTo)
        Divider(
            color = Color.Gray,
            modifier = Modifier
                .height(50.dp)
                .width(1.dp)
        )
        CarColumn(
            text1 = Duration.ofSeconds(duration.departure.toLong()).toString().drop(2),
            text2 = Duration.ofSeconds(duration.`return`.toLong()).toString().drop(2)
        )
        Divider(
            color = Color.Gray,
            modifier = Modifier
                .height(50.dp)
                .width(1.dp)
        )
        Text(
            text = "$price $currency",
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.SemiBold
        )
    }

}

@Composable
fun CarColumn(text1: String, text2: String) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.width(80.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = text1,
            style = MaterialTheme.typography.subtitle2,
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = text2,
            style = MaterialTheme.typography.subtitle2,
        )
    }
}

@Composable
fun CityIcon(cityImage: String) {
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
