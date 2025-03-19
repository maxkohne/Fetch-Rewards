package com.maxkohne.fetchrewards.data.items

import com.maxkohne.fetchrewards.data.items.api.ItemResponse
import com.maxkohne.fetchrewards.data.items.mapper.toEntity
import com.maxkohne.fetchrewards.data.items.mapper.toItem

// Fixtures
internal val itemResponse1 = ItemResponse(id = 1, listId = 1, name = "Item 1")
internal val itemResponse2 = ItemResponse(id = 2, listId = 2, name = "Item 2")
internal val itemEntity1 = itemResponse1.toEntity()
internal val itemEntity2 = itemResponse2.toEntity()
internal val item1 = itemEntity1.toItem()
internal val item2 = itemEntity2.toItem()