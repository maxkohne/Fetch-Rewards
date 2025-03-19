package com.maxkohne.fetchrewards.feature.items.details

import com.maxkohne.fetchrewards.data.items.Item

internal data class ItemsDetailsUiState(
    val listId: Long? = null,
    val name: String? = null,
)

internal fun ItemsDetailsUiState.fromItem(item: Item) = copy(
    listId = item.listId,
    name = item.name,
)