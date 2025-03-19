package com.maxkohne.fetchrewards.utils

import com.maxkohne.fetchrewards.core.ui.event.EventFlow
import com.maxkohne.fetchrewards.core.ui.snackbar.SnackBarEvent

internal object SnackBarController {

    private val _event = EventFlow<SnackBarEvent>()
    val eventFlow = _event.asFlow()

    fun sendEvent(event: SnackBarEvent) {
        _event.value = event
    }
}