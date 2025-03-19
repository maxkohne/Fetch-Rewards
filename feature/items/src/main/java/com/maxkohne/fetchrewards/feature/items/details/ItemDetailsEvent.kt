package com.maxkohne.fetchrewards.feature.items.details

internal sealed class ItemDetailsEvent {
    data object NotFound : ItemDetailsEvent()
}
