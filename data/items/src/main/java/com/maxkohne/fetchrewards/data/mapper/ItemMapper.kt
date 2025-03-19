package com.maxkohne.fetchrewards.data.mapper

import com.maxkohne.fetchrewards.core.database.items.ItemEntity
import com.maxkohne.fetchrewards.data.Item
import com.maxkohne.fetchrewards.data.api.ItemResponse

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