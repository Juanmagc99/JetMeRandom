package com.example.jetmerandom.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.jetmerandom.R

import com.example.jetmerandom.ui.SearchViewModel
import com.example.jetmerandom.data.DataSource.routesLocation
import com.example.jetmerandom.screens.components.RouteCard
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import java.time.Duration

@Composable
fun DetailsScreen(
    searchViewModel: SearchViewModel,
){
    val state = searchViewModel.uiState.collectAsState().value
    var from = routesLocation[state.flight!!.city_from]
    if (from == null){
        from = LatLng(40.4165, -3.70256)
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(from, 10f)
    }

    val scrollState = rememberScrollState()

    val routesList = routesLocation.values.toList()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(top = 8.dp, start = 8.dp, end = 8.dp, bottom = 8.dp)
            .fillMaxWidth()
            .verticalScroll(
                state = scrollState,
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .size(80.dp)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(35)),
                contentScale = ContentScale.Crop,
                painter = rememberAsyncImagePainter(model = state.flight.imageURL),
                contentDescription = null
            )
            Column() {
                Text(
                    text = state.flight.city_from + " / " + state.flight.city_to,
                    style = MaterialTheme.typography.h5,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = state.flight.routes[0].utc_departure.split("T")[0] + " / " +
                            state.flight.routes.last().utc_departure.split("T")[0],
                    style = MaterialTheme.typography.h6,
                )
            }
            IconButton(onClick = {
                searchViewModel.onLikedFlight(state.flight)
            }) {
                Icon(painter = painterResource(id = R.drawable.outline_push_pin_24), contentDescription = "Save a flight")
            }
        }

        DetailsCard(viewModel = searchViewModel)
        val handler = LocalUriHandler.current
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { handler.openUri(state.flight.deep_link) }
        ) {
            Text(text = "Buy it")
        }

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
        Column (
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ){

            if (flight != null) {
                Text(
                    text = stringResource(R.string.duration),
                    fontWeight = FontWeight.Bold
                )
                CardInfoRow(
                    text1 = "Time to arrive: ",
                    text2 = Duration.ofSeconds(flight.duration.departure.toLong()).toString()
                )

                CardInfoRow(
                    text1 = "Time to return: ",
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
                    Modifier
                        .fillMaxWidth()
                        .padding(end = 8.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(text = flight.price.toString() + " " + flight.currency.toString())
                }

                Divider(
                    color = Color.Gray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .padding(8.dp)
                )
                for (r in flight.routes!!){
                    RouteCard(route = r)
                }
            }

        }
    }
}

@Composable
fun CardInfoRow(text1:String, text2:String){
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = text1)
        Text(text = text2)
    }
}
