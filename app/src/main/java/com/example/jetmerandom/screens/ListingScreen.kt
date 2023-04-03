package com.example.jetmerandom

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jetmerandom.data.Data
import com.example.jetmerandom.data.DataSource.flights

@Composable
fun ListinScreen(
    viewModel: SearchViewModel
){
    println(flights)
    LazyColumn(modifier = Modifier.background(MaterialTheme.colors.background)) {
        items(flights){
            FlightItem(flight = it)
        }
    }
}

@Composable
fun FlightItem(flight: Data){
    var expanded by remember { mutableStateOf(false) }

    val color by animateColorAsState(
        targetValue = if (expanded) Color(0xFF129EAF) else MaterialTheme.colors.surface,
    )

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
            Text(text = flight.id)

        }
    }

}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun ListingPreview(){
    ListinScreen(
        viewModel = SearchViewModel()
    )
}