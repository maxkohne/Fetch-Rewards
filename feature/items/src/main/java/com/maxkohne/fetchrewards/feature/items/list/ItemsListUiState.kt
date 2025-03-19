package com.maxkohne.fetchrewards.feature.items.list

import com.maxkohne.fetchrewards.data.items.Item

internal data class ItemsListUiState(
    val searchQuery: String = "",
    val isLoading: Boolean = false
) {
    data class Section(
        val listId: String,
        val items: List<UiItem> = emptyList(),
    )

    data class UiItem(
        val id: Long,
        val listId: Long,
        val name: String
    )
}

internal fun Item.toUiItem(name: String) = ItemsListUiState.UiItem(
    id = id,
    listId = listId,
    name = name
)