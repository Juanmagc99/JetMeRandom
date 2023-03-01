package com.example.jetmerandom.screens.components

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.res.painterResource
import com.example.jetmerandom.R
import com.example.jetmerandom.SearchViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AutoCompleteSelect(
    label: String,
    options: List<String>,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
    viewModel: SearchViewModel
){

    var selectedItem by remember {
        mutableStateOf("")
    }

    var expanded by remember {
        mutableStateOf(false)
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded}
    ) {
        OutlinedTextField(
            value = selectedItem,
            onValueChange = { selectedItem = it },
            label = {
                Text(text = label)
            },
            trailingIcon = {
                if (!expanded){
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_search_24),
                        contentDescription = "arrow up icon")
                }else {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_cancel_24),
                        contentDescription = "search icon")
                }
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            maxLines = 1,
            keyboardActions = keyboardActions,
            keyboardOptions = keyboardOptions
        )

        val optionsFiltered =
            options.filter { it.contains(selectedItem, ignoreCase = true) }

        if (optionsFiltered.isNotEmpty()) {
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                optionsFiltered.forEach { selectionOption ->
                    DropdownMenuItem(
                        onClick = {
                            selectedItem = selectionOption
                            expanded = false
                            viewModel.onOriginSelected(selectedItem)
                        }
                    ) {
                        Text(text = selectionOption)
                    }
                }
            }
        }
    }

}