package com.maxkohne.fetchrewards.feature.items.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.maxkohne.fetchrewards.core.ui.event.ObserveAsEvent
import com.maxkohne.fetchrewards.core.ui.snackbar.SnackBarEvent
import com.maxkohne.fetchrewards.feature.list.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun ItemsListScreenRoute(
    showSnackBar: (SnackBarEvent) -> Unit,
    onItemClicked: (Long) -> Unit
) {
    val viewModel = hiltViewModel<ItemListViewModel>()

    ItemsListScreen(
        uiStateFlow = viewModel.uiStateFlow,
        itemsFlow = viewModel.itemsFlow,
        eventsFlow = viewModel.eventsFlow,
        showSnackBar = showSnackBar,
        onRefresh = viewModel::syncItems,
        onItemClicked = onItemClicked,
        onSearchConfirmed = viewModel::searchItems,
    )
}

@Composable
private fun ItemsListScreen(
    uiStateFlow: StateFlow<ItemsListUiState>,
    itemsFlow: StateFlow<List<ItemsListUiState.Section>>,
    eventsFlow: Flow<ItemsListEvent>,
    showSnackBar: (SnackBarEvent) -> Unit,
    onRefresh: () -> Unit,
    onItemClicked: (Long) -> Unit,
    onSearchConfirmed: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    ObserveEvents(eventsFlow, showSnackBar)
    val uiState by uiStateFlow.collectAsStateWithLifecycle()
    val items by itemsFlow.collectAsStateWithLifecycle()

    var text by rememberSaveable { mutableStateOf(uiState.searchQuery) }
    val currentFocus = LocalFocusManager.current

    LaunchedEffect(key1 = text) {
        // Debounce search query every 400 ms
        delay(400L)
        onSearchConfirmed(text)
    }

    Column {
        SearchBar(
            inputField = {
                SearchBarDefaults.InputField(
                    query = text,
                    onQueryChange = { text = it },
                    onSearch = {
                        currentFocus.clearFocus()
                        text = it
                    },
                    expanded = false,
                    onExpandedChange = { },
                    placeholder = { Text(stringResource(R.string.search)) },
                )
            },
            windowInsets = WindowInsets(0.dp),
            expanded = false,
            onExpandedChange = { },
            modifier = modifier
                .fillMaxWidth()
                .padding(
                    start = 8.dp,
                    end = 8.dp,
                    bottom = 12.dp
                ),
            content = {}
        )
        PullToRefreshBox(
            modifier = modifier.fillMaxSize(),
            isRefreshing = uiState.isLoading,
            onRefresh = onRefresh
        ) {
            when {
                items.isEmpty() -> {
                    EmptyView(
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.Center)
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items.forEach { section ->
                            // Header
                            stickyHeader {
                                SectionHeader(
                                    section = section,
                                    modifier = Modifier.animateItem()
                                )
                            }

                            // Items
                            val sectionItems = section.items
                            items(
                                count = sectionItems.size,
                                key = { index -> sectionItems[index].id },
                            ) { index ->
                                SectionItem(
                                    item = sectionItems[index],
                                    onItemClicked = onItemClicked,
                                    modifier = Modifier.animateItem()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyView(
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .wrapContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_no_items),
            contentDescription = null,
        )
        Text(
            text = "No items",
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
private fun SectionItem(
    item: ItemsListUiState.UiItem,
    onItemClicked: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    ListItem(
        headlineContent = { Text(text = item.name) },
        supportingContent = { Text(text = stringResource(R.string.id_placeholder, item.id)) },
        modifier = modifier.clickable { onItemClicked(item.id) }
    )
    HorizontalDivider(color = MaterialTheme.colorScheme.surfaceContainer)
}

@Composable
private fun SectionHeader(
    section: ItemsListUiState.Section,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shadowElevation = 1.dp
    ) {
        Text(
            text = "List ${section.listId}",
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .padding(12.dp)
        )
    }
}

@Composable
private fun ObserveEvents(
    events: Flow<ItemsListEvent>,
    showSnackBar: (SnackBarEvent) -> Unit
) {
    val syncError = stringResource(R.string.sync_error_general)
    val serverDownError = stringResource(R.string.sync_error_server_down)
    val connectionError = stringResource(R.string.sync_error_connection)
    val forbiddenError = stringResource(R.string.sync_error_forbidden)
    val storageFullError = stringResource(R.string.sync_error_storage_full)

    ObserveAsEvent(events) { event ->
        val message = when (event) {
            ItemsListEvent.SyncError.General -> syncError
            ItemsListEvent.SyncError.ServerDown -> serverDownError
            ItemsListEvent.SyncError.ConnectionError -> connectionError
            ItemsListEvent.SyncError.Forbidden -> forbiddenError
            ItemsListEvent.SyncError.StorageFull -> storageFullError
        }
        showSnackBar(SnackBarEvent(message = message))
    }
}

@Preview
@Composable
private fun ItemsListScreenPreview() {
    val items = listOf(
        ItemsListUiState.Section(
            listId = "1",
            items = buildList {
                repeat(20) {
                    add(
                        ItemsListUiState.UiItem(
                            id = it.toLong(),
                            listId = it.toLong(),
                            name = stringResource(R.string.item_placeholder, it)
                        )
                    )
                }
            }
        )
    )
    ItemsListScreen(
        uiStateFlow = MutableStateFlow(ItemsListUiState()),
        itemsFlow = MutableStateFlow(items),
        eventsFlow = emptyFlow(),
        showSnackBar = {},
        onRefresh = {},
        onItemClicked = {},
        onSearchConfirmed = {},
    )
}