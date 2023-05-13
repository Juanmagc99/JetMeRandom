package com.example.jetmerandom.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.rememberAsyncImagePainter
import com.example.jetmerandom.data.database.entities.FlightEntity
import com.example.jetmerandom.ui.LikedFlightViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking


@Composable
fun LikedScreen(likedFlightViewModel: LikedFlightViewModel) {
    likedFlightViewModel.getAllFlights()
    val state = likedFlightViewModel.uiState.collectAsState().value
    var results = state.flightsLiked
    LazyVerticalGrid(
        columns = GridCells.Fixed(count = 2),
    ) {
        items(results) { flight ->
            FlightItem(flight, likedFlightViewModel)
        }
    }


}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FlightItem(flight: FlightEntity, likedFlightViewModel: LikedFlightViewModel) {
    val handler = LocalUriHandler.current

    var showDialog by remember {
        mutableStateOf(false)
    }

    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .clip(RoundedCornerShape(15))
            .combinedClickable(
                onClick = {
                    showDialog = true
                },
                onLongClick = {
                    showDialog = true
                },
            ),

        ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                modifier = Modifier
                    .size(190.dp)
                    .clip(RoundedCornerShape(15)),
                contentScale = ContentScale.Crop,
                painter = rememberAsyncImagePainter(model = flight.imageURL),
                contentDescription = null
            )
            Text(
                text = flight.city_to,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 10.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(5.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column() {
                    Text(
                        text = flight.depDate.split("T")[0],
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = flight.retDate.split("T")[0],
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Text(
                    text = flight.price.toString() + flight.currency,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
    flightDialog(flight, showDialog, {showDialog = false },{ handler.openUri(flight.deep_link) },{likedFlightViewModel.onDeleteFlight(flight)})
}

@Composable
fun flightDialog(
    flight: FlightEntity,
    show: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    onDelete: () -> Unit,
) {
    if (show){
        AlertDialog(
            dismissButton = {
                OutlinedButton(onClick = onDelete ) {
                    Text(text = "Delete")
                }
            },
            confirmButton = {
                Button(onClick =  onConfirm ) {
                    Text(text = "Buy")
                }
            },
            onDismissRequest = { onDismiss() },
            title = {
                        Text(
                            text = flight.city_from + "/" + flight.city_to,
                            style = MaterialTheme.typography.h6
                        )
                    },
            text = {
                Column(
                    modifier = Modifier
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    InfoDialog(
                        text1 = "Departure",
                        text2 = flight.depDate.split("T")[0] + " " + flight.depDate.split("T")[1].dropLast(8)
                    )
                    InfoDialog(
                        text1 = "Return",
                        text2 = flight.retDate.split("T")[0] + " " + flight.depDate.split("T")[1].dropLast(8)
                    )
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                    )
                    InfoDialog(
                        text1 = "Stops: ",
                        text2 = flight.n_stops.toString()
                    )
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                    )
                    InfoDialog(
                        text1 = "Total price for " + flight.n_passengers.toString() + " passengers",
                        text2 = flight.price.toString() + " " + flight.currency
                    )
                }
            }
        )
    }
}

@Composable
fun InfoDialog(text1: String, text2: String){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = text1, fontWeight = FontWeight.SemiBold)
        Text(text = text2)
    }
}