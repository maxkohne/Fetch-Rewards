package com.maxkohne.fetchrewards.data.items

import com.maxkohne.fetchrewards.data.items.api.ItemResponse
import com.maxkohne.fetchrewards.data.items.mapper.toEntity
import com.maxkohne.fetchrewards.data.items.mapper.toItem

// Fixtures
internal val itemResponse1 = generateItemResponse(1)
internal val itemResponse2 = generateItemResponse(2)
internal val itemEntity1 = itemResponse1.toEntity()
internal val itemEntity2 = itemResponse2.toEntity()
internal val item1 = itemEntity1.toItem()
internal val item2 = itemEntity2.toItem()

private fun generateItemResponse(id: Long): ItemResponse {
    return ItemResponse(
        id = id,
        listId = id,
        name = "Item $id"
    )
}