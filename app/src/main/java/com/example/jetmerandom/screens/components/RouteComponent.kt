package com.example.jetmerandom.screens.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.jetmerandom.data.Flight
import com.example.jetmerandom.data.Route

@Composable
fun CardRoute(flight: Flight){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .padding(top = 4.dp, bottom = 4.dp, start = 16.dp, end = 16.dp)
    ) {
        RouteInfo(
            cityFrom = flight.routes[0].cityFrom,
            cityTo = flight.routes[0].cityTo,
            departure = flight.routes[0].utc_departure.split("T")[1].dropLast(5),
            arrival = flight.routes[0].utc_arrival.split("T")[1].dropLast(5)
        )
        RouteInfo(
            cityFrom = flight.routes.last().cityFrom,
            cityTo = flight.routes.last().cityTo,
            departure = flight.routes.last().utc_departure.split("T")[1].dropLast(5),
            arrival = flight.routes.last().utc_arrival.split("T")[1].dropLast(5)
        )
    }
}

@Composable
fun CardRouteUniversal(route: Route){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .padding(top = 4.dp, bottom = 4.dp, start = 16.dp, end = 16.dp)
    ) {
        RouteInfo(
            cityFrom = route.cityFrom,
            cityTo = route.cityTo,
            departure = route.utc_departure.split("T")[1].dropLast(5),
            arrival = route.utc_arrival.split("T")[1].dropLast(5)
        )
    }
}

@Composable
fun RouteInfo(cityFrom: String,
              cityTo: String,
              departure: String,
              arrival: String
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = cityFrom,
                modifier = Modifier.width(90.dp),
                textAlign = TextAlign.Center,
                style = TextStyle(fontWeight = FontWeight.SemiBold)
            )
            Text(text = departure, style = MaterialTheme.typography.body2)
        }

        Icon(
            imageVector = Icons.Default.ArrowForward,
            contentDescription = null,
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = cityTo,
                modifier = Modifier.width(90.dp),
                textAlign = TextAlign.Center,
                style = TextStyle(fontWeight = FontWeight.SemiBold)
            )
            Text(text = arrival, style = MaterialTheme.typography.body2)
        }

    }
}
