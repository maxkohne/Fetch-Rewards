package com.maxkohne.fetchrewards.data.items.mapper

import com.maxkohne.fetchrewards.core.database.items.ItemEntity
import com.maxkohne.fetchrewards.data.items.Item
import com.maxkohne.fetchrewards.data.items.api.ItemResponse

internal fun ItemResponse.toEntity() = ItemEntity(
    id = id,
    listId = listId,
    name = name
)

internal fun ItemEntity.toItem() = Item(
    id = id,
    listId = listId,
    name = name
)