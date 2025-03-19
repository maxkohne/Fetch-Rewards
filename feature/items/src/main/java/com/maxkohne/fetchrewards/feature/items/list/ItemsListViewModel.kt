package com.maxkohne.fetchrewards.feature.items.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maxkohne.fetchrewards.core.sync.common.SyncError
import com.maxkohne.fetchrewards.core.sync.common.SyncResult
import com.maxkohne.fetchrewards.core.ui.event.EventFlow
import com.maxkohne.fetchrewards.core.util.DefaultDispatcher
import com.maxkohne.fetchrewards.data.items.Item
import com.maxkohne.fetchrewards.data.items.ItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
internal class ItemListViewModel @Inject constructor(
    private val itemRepository: ItemRepository,
    private val savedStateHandle: SavedStateHandle,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {

    companion object {
        private const val STATE_SEARCH_QUERY = "search_query"
    }

    private var searchQuery: String
        set(value) {
            savedStateHandle[STATE_SEARCH_QUERY] = value
        }
        get() = savedStateHandle.get<String?>(STATE_SEARCH_QUERY).orEmpty()
    val itemsFlow = savedStateHandle.getStateFlow(STATE_SEARCH_QUERY, searchQuery)
        .flatMapLatest { searchQuery ->
            itemRepository.getAllItemsFlow().map { items ->
                items.toSections(searchQuery = searchQuery)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList()
        )

    private val _uiStateFlow = MutableStateFlow(ItemsListUiState())
    val uiStateFlow = _uiStateFlow.asStateFlow()

    private val _eventsFlow = EventFlow<ItemsListEvent>()
    val eventsFlow = _eventsFlow.asFlow()

    private var syncJob: Job? = null

    init {
        syncItems()
    }

    fun searchItems(searchQuery: String) {
        this.searchQuery = searchQuery
    }

    fun syncItems() {
        // Bail if a sync is already running. No need to execute multiple at once.
        if (syncJob?.isActive == true) return

        syncJob = viewModelScope.launch {
            _uiStateFlow.update { it.copy(isLoading = true) }

            when (val result = itemRepository.syncItems()) {
                is SyncResult.Failure -> {
                    // Handle error
                    val error = when (result.error) {
                        SyncError.General -> ItemsListEvent.SyncError.General
                        SyncError.ServerDown -> ItemsListEvent.SyncError.ServerDown
                        SyncError.ConnectivityError -> ItemsListEvent.SyncError.ConnectionError
                        SyncError.Forbidden -> ItemsListEvent.SyncError.Forbidden
                        SyncError.StorageFull -> ItemsListEvent.SyncError.StorageFull
                    }
                    _eventsFlow.setValue(error)
                }

                // Don't need to do anything on success
                is SyncResult.Success -> Unit
            }

            _uiStateFlow.update { it.copy(isLoading = false) }
        }
    }

    private suspend fun List<Item>.toSections(searchQuery: String): List<ItemsListUiState.Section> {
        val isSearchApplied = searchQuery.isNotBlank()

        // Run on default dispatcher since these are heavy in-memory computations
        return withContext(defaultDispatcher) {
            mapNotNull {
                // Filter out items without names
                val name = it.name
                if (name.isNullOrBlank()) return@mapNotNull null

                // Filter by search query
                if (isSearchApplied && !name.contains(searchQuery, ignoreCase = true)) {
                    return@mapNotNull null
                }

                // Convert to ui item
                it.toUiItem(name)
            }.sortedWith(
                // Sort by listID then name
                compareBy<ItemsListUiState.UiItem> { it.listId }
                    .thenBy { it.name }
            ).groupBy { it.listId } // Group by listID
                .map { (listId, items) ->
                    // Convert to section
                    ItemsListUiState.Section(
                        listId = listId.toString(),
                        items = items
                    )
                }
        }
    }
}