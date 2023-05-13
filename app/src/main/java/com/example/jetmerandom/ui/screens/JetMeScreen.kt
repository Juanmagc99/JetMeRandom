package com.example.jetmerandom.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.jetmerandom.ListinScreen
import com.example.jetmerandom.R
import com.example.jetmerandom.data.DataSource.flightsToCompare
import com.example.jetmerandom.screens.DetailsScreen
import com.example.jetmerandom.screens.SearchScreen
import com.example.jetmerandom.ui.LikedFlightViewModel
import com.example.jetmerandom.ui.SearchViewModel


enum class JetMeScreen() {
    Search,
    Listing,
    Details,
    Liked,
    Comparate
}

@Composable
fun JetMeRandomAppBar(
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    navigateLiked: () -> Unit = {},
    navigateComparator: () -> Unit = {},
    modifier: Modifier = Modifier,
    currentScreen: String,
    viewModel: SearchViewModel
) {

    var compare = viewModel.uiState.collectAsState().value.readyToCompare

    TopAppBar(
        title = { Text(stringResource(com.example.jetmerandom.R.string.app_name)) },
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(com.example.jetmerandom.R.string.back_button)
                    )
                }
            }
        },
        actions = {
            IconButton(
                onClick =  navigateLiked,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_save_24),
                    contentDescription = stringResource(R.string.liked_bar_label)
                )
            }
            if (currentScreen == JetMeScreen.Listing.name && compare){
                IconButton(
                    onClick =  navigateComparator,
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.icons8_compare_64),
                        contentDescription = stringResource(R.string.liked_bar_label)
                    )
                }
            }
        }
    )
}


@Composable
fun JetMeRandomAppTotal(
    searchViewModel : SearchViewModel,
    likedFlightViewModel: LikedFlightViewModel
){

    val navController = rememberNavController()

    val backStackEntry by navController.currentBackStackEntryAsState()

    val currentScreen = backStackEntry?.destination?.route ?: JetMeScreen.Search.name

    Scaffold(
        topBar = {
            JetMeRandomAppBar(
                canNavigateBack = navController.previousBackStackEntry != null ,
                navigateUp = { navController.navigateUp() },
                currentScreen = currentScreen,
                navigateLiked = { navController.navigate(JetMeScreen.Liked.name) },
                navigateComparator = { navController.navigate(JetMeScreen.Comparate.name) },
                viewModel = searchViewModel
            )
        }
    ) { innerPadding ->
        val uiState by searchViewModel.uiState.collectAsState()

        NavHost(
            navController = navController,
            startDestination = JetMeScreen.Search.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = JetMeScreen.Search.name) {
                SearchScreen (
                    viewModel = searchViewModel,
                    onNextButtonClicked = {navController.navigate(JetMeScreen.Listing.name)}
                )
            }
            composable(route = JetMeScreen.Listing.name) {
                ListinScreen(
                    viewModel = searchViewModel,
                    onDetailsClicked = {
                        navController.navigate(JetMeScreen.Details.name)
                    }
                )
            }
            composable(route = JetMeScreen.Details.name) {
                DetailsScreen(
                    searchViewModel = searchViewModel,
                )
            }
            composable(route = JetMeScreen.Liked.name) {

                LikedScreen(
                    likedFlightViewModel = likedFlightViewModel,
                )
            }
            composable(route = JetMeScreen.Comparate.name) {

                CompareScreen(
                    searchViewModel = searchViewModel,
                )
            }
        }
    }
}
