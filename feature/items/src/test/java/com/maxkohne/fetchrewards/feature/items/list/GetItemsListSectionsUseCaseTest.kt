package com.maxkohne.fetchrewards.feature.items.list

import com.google.common.truth.Truth
import com.maxkohne.fetchrewards.data.items.Item
import com.maxkohne.fetchrewards.feature.items.list.usecase.GetItemsListSectionsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

internal class GetItemsListSectionsUseCaseTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var getItemsListSectionsUseCase: GetItemsListSectionsUseCase

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getItemsListSectionsUseCase = GetItemsListSectionsUseCase(
            defaultDispatcher = testDispatcher
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `execute without search`() = runTest {
        val items = listOf(
            generateItem(id = 1, listId = 2),
            generateItem(id = 2, listId = 3),
            generateItem(id = 3, listId = 1),
            generateItem(id = 4, listId = 1, name = null),
            generateItem(id = 5, listId = 1, name = "  "),
            generateItem(id = 6, listId = 1),
            generateItem(id = 25, listId = 1),
        )
        val sections = getSections(items)

        val result = getItemsListSectionsUseCase.execute(
            items = items,
            searchQuery = ""
        )
        Truth.assertThat(result).isEqualTo(sections)
    }

    @Test
    fun `execute with search`() = runTest {
        val searchQuery = "4"
        val items = listOf(
            generateItem(id = 1, listId = 2),
            generateItem(id = 2, listId = 3),
            generateItem(id = 3, listId = 1),
            generateItem(id = 4, listId = 1, name = null),
            generateItem(id = 5, listId = 1, name = "  "),
            generateItem(id = 6, listId = 1),
            generateItem(id = 25, listId = 1),
        )
        val sections = getSections(items, searchQuery)

        val result = getItemsListSectionsUseCase.execute(
            items = items,
            searchQuery = searchQuery
        )
        Truth.assertThat(result).isEqualTo(sections)
    }

    // TODO it's probably better to output sections manually rather than copy/paste from the real implementation
    private fun getSections(items: List<Item>, searchQuery: String = ""): List<ItemsListUiState.Section> {
        val sortComparator = compareBy<ItemsListUiState.UiItem> { it.listId }
            .thenBy { it.name }

        return items.asSequence()
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

    private fun generateItem(id: Long, listId: Long = id, name: String? = "Item $id"): Item {
        return Item(
            id = id,
            listId = listId,
            name = name
        )
    }
}