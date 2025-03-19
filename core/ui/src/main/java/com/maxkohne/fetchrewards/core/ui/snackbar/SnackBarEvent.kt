package com.maxkohne.fetchrewards.core.ui.snackbar

data class SnackBarEvent(
    val message: String,
    val action: SnackBarAction? = null
)

data class SnackBarAction(
    val name: String,
    val performAction: () -> Unit
)