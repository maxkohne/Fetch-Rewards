package com.maxkohne.fetchrewards.feature.items.list.usecase

import com.maxkohne.fetchrewards.core.util.DefaultDispatcher
import com.maxkohne.fetchrewards.data.items.Item
import com.maxkohne.fetchrewards.feature.items.list.ItemsListUiState
import com.maxkohne.fetchrewards.feature.items.list.toUiItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class GetItemsListSectionsUseCase @Inject constructor(
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) {
    suspend fun execute(items: List<Item>, searchQuery: String): List<ItemsListUiState.Section> {
        val sortComparator = compareBy<ItemsListUiState.UiItem> { it.listId }
            .thenBy { it.name }

        // Run on default dispatcher since these are heavy in-memory computations
        return withContext(defaultDispatcher) {
            items.asSequence()
                .filter { !it.name.isNullOrBlank() }
                .filter { searchQuery.isBlank() || it.name!!.contains(searchQuery, ignoreCase = true) }
                .map { it.toUiItem(it.name!!) }
                .sortedWith(sortComparator)
                .groupBy { it.listId }
                .map { (listId, items) ->
                    ItemsListUiState.Section(
                        listId = "$listId",
                        items = items
                    )
                }
        }
    }

}