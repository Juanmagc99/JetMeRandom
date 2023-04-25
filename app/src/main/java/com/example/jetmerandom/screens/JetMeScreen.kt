package com.example.jetmerandom.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.jetmerandom.ListinScreen
import com.example.jetmerandom.SearchViewModel
import com.example.jetmerandom.screens.DetailsScreen
import com.example.jetmerandom.screens.SearchScreen
import java.time.LocalDate


enum class JetMeScreen() {
    Search,
    Listing,
    Details
}

@Composable
fun JetMeRandomAppBar(
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    currentScreen: String,
) {
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
        }
    )
}


@Composable
fun JetMeRandomApp(modifier: Modifier = Modifier, viewModel: SearchViewModel = androidx.lifecycle.viewmodel.compose.viewModel()){

    val navController = rememberNavController()

    val backStackEntry by navController.currentBackStackEntryAsState()

    val currentScreen = backStackEntry?.destination?.route ?: JetMeScreen.Search.name

    Scaffold(
        topBar = {
            JetMeRandomAppBar(
                canNavigateBack = navController.previousBackStackEntry != null ,
                navigateUp = { navController.navigateUp() },
                currentScreen = currentScreen
            )
        }
    ) { innerPadding ->
        val uiState by viewModel.uiState.collectAsState()

        NavHost(
            navController = navController,
            startDestination = JetMeScreen.Search.name,
            modifier = modifier.padding(innerPadding)
        ) {
            composable(route = JetMeScreen.Search.name) {
                SearchScreen (
                    viewModel = viewModel,
                    onNextButtonClicked = {navController.navigate(JetMeScreen.Listing.name)}
                )
            }
            composable(route = JetMeScreen.Listing.name) {
                ListinScreen(
                    viewModel = viewModel,
                    onDetailsClicked = {
                        navController.navigate(JetMeScreen.Details.name)
                    }
                )
            }
            composable(route = JetMeScreen.Details.name) {
                DetailsScreen(
                    viewModel = viewModel
                )
            }
        }
    }
}
