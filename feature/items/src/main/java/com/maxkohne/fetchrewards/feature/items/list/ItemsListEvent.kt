package com.maxkohne.fetchrewards.feature.items.list

internal sealed class ItemsListEvent {
    sealed class SyncError : ItemsListEvent() {
        data object General : ItemsListEvent()
        data object ServerDown : ItemsListEvent()
        data object ConnectionError : ItemsListEvent()
        data object Forbidden : ItemsListEvent()
        data object StorageFull : ItemsListEvent()
    }
}
