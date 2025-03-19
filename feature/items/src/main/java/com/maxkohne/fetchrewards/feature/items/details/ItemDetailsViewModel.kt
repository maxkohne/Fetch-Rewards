package com.maxkohne.fetchrewards.feature.items.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maxkohne.fetchrewards.core.ui.event.EventFlow
import com.maxkohne.fetchrewards.data.items.ItemRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = ItemDetailsViewModel.ItemDetailsViewModelFactory::class)
internal class ItemDetailsViewModel @AssistedInject constructor(
    private val repository: ItemRepository,
    @Assisted private val itemId: Long,
) : ViewModel() {

    private val _uiStateFlow = MutableStateFlow(ItemsDetailsUiState())
    val uiStateFlow = _uiStateFlow.asStateFlow()

    private val _eventsFlow = EventFlow<ItemDetailsEvent>()
    val eventsFlow = _eventsFlow.asFlow()

    init {
        getItem(itemId)
    }

    fun getItem(itemId: Long) {
        viewModelScope.launch {
            repository.getItemFlow(itemId).collect { item ->
                if (item == null) {
                    _eventsFlow.setValue(ItemDetailsEvent.NotFound)
                } else {
                    _uiStateFlow.update { it.fromItem(item) }
                }
            }
        }
    }

    @AssistedFactory
    interface ItemDetailsViewModelFactory {
        fun create(itemId: Long): ItemDetailsViewModel
    }
}