package com.maxkohne.fetchrewards.core.sync.common

sealed class SyncResult {
    data object Success : SyncResult()
    data class Failure(val error: SyncError) : SyncResult()
}

sealed class SyncError {
    data object General : SyncError()
    data object ConnectivityError : SyncError()
    data object ServerDown : SyncError()
    data object Forbidden : SyncError()
    data object StorageFull : SyncError()
}