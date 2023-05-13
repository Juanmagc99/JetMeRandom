package com.example.jetmerandom.ui.screens

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
import com.example.jetmerandom.data.DataSource.flightsToCompare
import com.example.jetmerandom.screens.CardInfoRow
import com.example.jetmerandom.screens.components.RouteCard
import com.example.jetmerandom.ui.SearchViewModel
import java.time.Duration


@Composable
fun CompareScreen(searchViewModel: SearchViewModel) {
    val scrollState = rememberScrollState()

    var state = searchViewModel.uiState.collectAsState().value
    val handler = LocalUriHandler.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(top = 8.dp, start = 8.dp, end = 8.dp, bottom = 8.dp)
            .fillMaxWidth()
            .verticalScroll(
                state = scrollState,
            )
    ) {
        for (f in flightsToCompare) {

            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        modifier = Modifier
                            .size(80.dp)
                            .padding(8.dp)
                            .clip(RoundedCornerShape(35)),
                        contentScale = ContentScale.Crop,
                        painter = rememberAsyncImagePainter(model = f.imageURL),
                        contentDescription = null
                    )
                    Column() {
                        Text(
                            text = f.city_from + " / " + f.city_to,
                            style = MaterialTheme.typography.h5,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = f.routes[0].utc_departure.split("T")[0] + " / " +
                                    f.routes.last().utc_departure.split("T")[0],
                            style = MaterialTheme.typography.h6,
                        )
                        IconButton(onClick = {
                            handler.openUri(f.deep_link)
                        }) {
                            Icon(painter = painterResource(id = R.drawable.icons8_buy_64), contentDescription = "Save a flight")
                        }
                    }
                }
                Card(
                    elevation = 4.dp,
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.duration),
                            fontWeight = FontWeight.Bold
                        )
                        CardInfoRow(
                            text1 = "Time to arrive: ",
                            text2 = Duration.ofSeconds(f.duration.departure.toLong()).toString().drop(2)
                        )

                        CardInfoRow(
                            text1 = "Time to return: ",
                            text2 = Duration.ofSeconds(f.duration.`return`.toLong()).toString().drop(2)
                        )
                        Text(
                            text = stringResource(R.string.prices),
                            fontWeight = FontWeight.Bold
                        )
                        CardInfoRow(
                            text1 = "Adults: ",
                            text2 = state.qAdults.toString() + " x " + f.fare.adults.toString()
                        )
                        CardInfoRow(
                            text1 = "Childs: ",
                            text2 = state.qChilds.toString() + " x " + f.fare.children.toString()
                        )
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(end = 8.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text(text = f.price.toString() + " " + f.currency.toString())
                        }

                        Divider(
                            color = Color.Gray,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(2.dp)
                                .padding(8.dp)
                        )
                        for (r in f.routes!!) {
                            RouteCard(route = r)
                        }
                    }
                }

            }
        }
    }
}