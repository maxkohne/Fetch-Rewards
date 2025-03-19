package com.maxkohne.fetchrewards.feature.items.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.maxkohne.fetchrewards.core.ui.event.ObserveAsEvent
import com.maxkohne.fetchrewards.core.ui.snackbar.SnackBarEvent
import com.maxkohne.fetchrewards.feature.list.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun ItemsDetailsScreenRoute(
    itemId: Long,
    showSnackBar: (SnackBarEvent) -> Unit,
) {
    val viewModel = hiltViewModel<ItemDetailsViewModel, ItemDetailsViewModel.ItemDetailsViewModelFactory> { factory ->
        factory.create(itemId)
    }

    ItemsDetailsScreen(
        itemId = itemId,
        uiStateFlow = viewModel.uiStateFlow,
        eventsFlow = viewModel.eventsFlow,
        showSnackBar = showSnackBar,
    )
}

@Composable
private fun ItemsDetailsScreen(
    itemId: Long,
    uiStateFlow: StateFlow<ItemsDetailsUiState>,
    eventsFlow: Flow<ItemDetailsEvent>,
    showSnackBar: (SnackBarEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    ObserveEvents(eventsFlow, showSnackBar)
    val uiState by uiStateFlow.collectAsStateWithLifecycle()

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "List ${uiState.listId}")
        Text(text = uiState.name.toString())
        Text(text = "ID $itemId")
    }
}

@Composable
private fun ObserveEvents(
    events: Flow<ItemDetailsEvent>,
    showSnackBar: (SnackBarEvent) -> Unit
) {
    val notFoundError = stringResource(R.string.error_not_found)

    ObserveAsEvent(events) { event ->
        val message = when (event) {
            ItemDetailsEvent.NotFound -> notFoundError
        }
        showSnackBar(SnackBarEvent(message = message))
    }
}

@Preview
@Composable
private fun ItemDetailsScreenPreview() {
    ItemsDetailsScreen(
        uiStateFlow = MutableStateFlow(ItemsDetailsUiState()),
        eventsFlow = emptyFlow(),
        showSnackBar = {},
        itemId = 1
    )
}