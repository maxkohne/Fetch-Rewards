package com.maxkohne.fetchrewards.core.ui.event

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

class EventFlow<T> {
    private val bufferedChannel = Channel<T>(Channel.Factory.BUFFERED)

    var value: T
        set(value) {
            bufferedChannel.trySend(value)
        }
        get() {
            return bufferedChannel.tryReceive().getOrThrow()
        }

    suspend fun setValue(value: T) {
        bufferedChannel.send(value)
    }

    fun asFlow(): Flow<T> = bufferedChannel.receiveAsFlow()
}