package com.maxkohne.fetchrewards

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.maxkohne.core.designsystem.theme.FetchRewardsTheme
import com.maxkohne.fetchrewards.core.ui.event.ObserveAsEvent
import com.maxkohne.fetchrewards.feature.items.details.ItemsDetailsScreenRoute
import com.maxkohne.fetchrewards.feature.items.list.ItemsListScreenRoute
import com.maxkohne.fetchrewards.navigation.ItemsRoute
import com.maxkohne.fetchrewards.utils.SnackBarController
import kotlinx.coroutines.launch

@Composable
internal fun MainScreen() {
    val snackBarHostState = remember { SnackbarHostState() }
    ObserveSnackBarEvents(snackBarHostState)
    val navController = rememberNavController()

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val showBackButton by remember(currentBackStackEntry) {
        derivedStateOf { navController.previousBackStackEntry != null }
    }

    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                navigationIcon = {
                    if (showBackButton) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null
                            )
                        }
                    }
                },
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState,
                modifier = Modifier.imePadding()
            )
        }
    ) { innerPadding ->
        MainNavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController
        )
    }
}

@Composable
private fun MainNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = ItemsRoute.List
    ) {
        composable<ItemsRoute.List> {
            ItemsListScreenRoute(
                showSnackBar = SnackBarController::sendEvent,
                onItemClicked = { navController.navigate(ItemsRoute.Details(it)) }
            )
        }
        composable<ItemsRoute.Details> {
            ItemsDetailsScreenRoute(
                itemId = it.toRoute<ItemsRoute.Details>().id,
                showSnackBar = SnackBarController::sendEvent,
            )
        }
    }
}

@Composable
private fun ObserveSnackBarEvents(snackBarHostState: SnackbarHostState) {
    val scope = rememberCoroutineScope()
    ObserveAsEvent(SnackBarController.eventFlow) { snackBarEvent ->
        scope.launch {
            // Dismiss current SnackBar if one is already showing
            snackBarHostState.currentSnackbarData?.dismiss()

            // Show new SnackBar
            val result = snackBarHostState.showSnackbar(
                message = snackBarEvent.message,
                actionLabel = snackBarEvent.action?.name,
                duration = SnackbarDuration.Short
            )

            // Handle SnackBar result
            if (result == SnackbarResult.ActionPerformed) {
                snackBarEvent.action?.performAction?.invoke()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    FetchRewardsTheme {
        MainScreen()
    }
}