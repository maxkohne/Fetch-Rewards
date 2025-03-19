package com.maxkohne.fetchrewards.navigation

import kotlinx.serialization.Serializable

internal sealed class ItemsRoute {
    @Serializable
    internal data object List : ItemsRoute()

    @Serializable
    internal data class Details(
        val id: Long
    ) : ItemsRoute()
}