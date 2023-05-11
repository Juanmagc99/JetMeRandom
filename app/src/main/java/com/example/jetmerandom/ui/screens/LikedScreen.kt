package com.example.jetmerandom.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
    val results2 = results + results
    println(results)
    LazyVerticalGrid(
        columns = GridCells.Fixed(count = 2),
    ) {
        items(results2) { flight ->
            FlightItem(flight)
        }
    }


}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FlightItem(flight: FlightEntity) {
    val handler = LocalUriHandler.current



    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .clip(RoundedCornerShape(15))
            .combinedClickable(
                onClick = {
                    handler.openUri(flight.deep_link)
                },
                onLongClick = {},
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
                        text = flight.depDate,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = flight.depDate,
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
}
