package com.example.jetmerandom.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jetmerandom.R

import com.example.jetmerandom.SearchViewModel
import com.example.jetmerandom.data.DataSource.flights
import com.example.jetmerandom.data.DataSource.routesLocation
import com.example.jetmerandom.screens.components.CardRouteUniversal
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolygonOptions
import com.google.maps.android.compose.*
import java.time.Duration

@Composable
fun DetailsScreen(viewModel: SearchViewModel){
    val state = viewModel.uiState.collectAsState().value
    var from = routesLocation[state.flight!!.city_from]
    if (from == null){
        from = LatLng(40.4165, -3.70256)
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(from, 10f)
    }


    val routesList = routesLocation.values.toList()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(top = 8.dp, start = 8.dp, end = 8.dp, bottom = 8.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = state.flight.city_from + " / " + state.flight.city_to,
            style = MaterialTheme.typography.h5,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        )
        Text(
            text = state.flight.routes[0].utc_departure.split("T")[0] + " / " +
                    state.flight.routes.last().utc_departure.split("T")[0],
            style = MaterialTheme.typography.h6,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        )

        DetailsCard(viewModel = viewModel)
       GoogleMap(
            modifier = Modifier
                .width(450.dp)
                .height(500.dp),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(mapType = MapType.NORMAL)
        ){
            for (r in routesLocation){
                Marker(
                    state = MarkerState(position = r.value),
                    title = r.key,
                )
            }
            println(routesList)
            Polyline(
                points = routesList,
                geodesic = true
            )
        }
    }

}

@Composable
fun DetailsCard(viewModel: SearchViewModel){
    val state = viewModel.uiState.collectAsState().value
    val flight = state.flight
    Card(
        elevation = 4.dp,
        modifier = Modifier.padding(bottom = 8.dp)
    ){
        Column (modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
        ){

            if (flight != null) {
                Text(
                    text = stringResource(R.string.duration),
                    fontWeight = FontWeight.Bold
                )
                CardInfoRow(
                    text1 = "Time to go: ",
                    text2 = Duration.ofSeconds(flight.duration.departure.toLong()).toString()
                )
                CardInfoRow(
                    text1 = "Time to come: ",
                    text2 = Duration.ofSeconds(flight.duration.`return`.toLong()).toString()
                )
                Text(
                    text = stringResource(R.string.prices),
                    fontWeight = FontWeight.Bold
                )
                CardInfoRow(
                    text1 = "Adults: ",
                    text2 = state.qAdults.toString() + " x " + flight.fare.adults.toString()
                )
                CardInfoRow(
                    text1 = "Childs: ",
                    text2 = state.qChilds.toString() + " x " + flight.fare.children.toString()
                )
                Row(
                    Modifier.fillMaxWidth().padding(end = 8.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(text = flight.price.toString() + " " + flight.currency.toString())
                }
                println(flight.routes)
                for (r in flight.routes!!){
                    CardRouteUniversal(route = r)
                }
            }

        }
    }
}

@Composable
fun CardInfoRow(text1:String, text2:String){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = text1)
        Text(text = text2)
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun ListingPreview(){
    DetailsScreen(
        viewModel = SearchViewModel()
    )
}