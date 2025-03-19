package com.maxkohne.fetchrewards.feature.items.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maxkohne.fetchrewards.core.ui.event.EventFlow
import com.maxkohne.fetchrewards.data.items.ItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ItemDetailsViewModel @Inject constructor(
    private val repository: ItemRepository,
) : ViewModel() {

    private val _uiStateFlow = MutableStateFlow(ItemsDetailsUiState())
    val uiStateFlow = _uiStateFlow.asStateFlow()

    private val _eventsFlow = EventFlow<ItemDetailsEvent>()
    val eventsFlow = _eventsFlow.asFlow()

    fun getItem(itemId: Long) {
        viewModelScope.launch {
            repository.getItemFlow(itemId).collect { item ->
                if (item == null) {
                    _eventsFlow.setValue(ItemDetailsEvent.NotFound)
                    return@collect
                }

                _uiStateFlow.update {
                    it.copy(
                        name = item.name,
                        listId = item.listId,
                    )
                }
            }
        }
    }
}