package com.maxkohne.fetchrewards.data.items.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ItemResponse(
    @SerialName("id") val id: Long,
    @SerialName("listId") val listId: Long,
    @SerialName("name") val name: String?
)