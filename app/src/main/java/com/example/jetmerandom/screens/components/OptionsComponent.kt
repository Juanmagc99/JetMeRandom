package com.example.jetmerandom.screens.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jetmerandom.R
import com.example.jetmerandom.SearchViewModel
import com.maxkeppeker.sheets.core.models.base.IconSource
import com.maxkeppeker.sheets.core.models.base.SheetState
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.input.InputDialog
import com.maxkeppeler.sheets.input.models.InputCheckboxGroup
import com.maxkeppeler.sheets.input.models.InputHeader
import com.maxkeppeler.sheets.input.models.InputSelection
import com.maxkeppeler.sheets.list.ListDialog
import com.maxkeppeler.sheets.list.models.ListOption
import com.maxkeppeler.sheets.list.models.ListSelection
import com.maxkeppeler.sheets.option.OptionDialog
import com.maxkeppeler.sheets.option.OptionView
import com.maxkeppeler.sheets.option.models.*


@Composable
fun OptionsPick(viewModel: SearchViewModel){

     val listState = rememberSheetState()
     OutlinedButton(
         onClick = {
             listState.show()
         },
         contentPadding = PaddingValues(
             start = 20.dp,
             top = 12.dp,
             end = 20.dp,
             bottom = 12.dp
         ),
         modifier = Modifier.size(130.dp, 60.dp),
         border = ButtonDefaults.outlinedBorder
     ) {
         Icon(
             painterResource(id = R.drawable.baseline_airline_seat_recline_normal_24),
             contentDescription = "Date picker icon",

             )
         Spacer(Modifier.size(ButtonDefaults.IconSpacing))
         Text(text = "Cabin type")
     }

    
    val options = listOf(
        Option(
            IconSource(R.drawable.baseline_navigate_next_24),
            titleText = "Economy"
        ),
        Option(
            IconSource(R.drawable.baseline_navigate_next_24),
            titleText = "Economy premium",
        ),
        Option(
            IconSource(R.drawable.baseline_navigate_next_24),
            titleText = "Business",
        ),
        Option(
            IconSource(R.drawable.baseline_navigate_next_24),
            titleText = "First class",
        ),
    )

    OptionDialog(
        state = listState,
        selection = OptionSelection.Single(options) { index, option ->
            when (index) {
                0 -> viewModel.setCabins("M")
                1 -> viewModel.setCabins("W")
                2 -> viewModel.setCabins("C")
                else -> { // Note the block
                    viewModel.setCabins("F")
                }
            }
        },
        config = OptionConfig(mode = DisplayMode.LIST)
    )
}

