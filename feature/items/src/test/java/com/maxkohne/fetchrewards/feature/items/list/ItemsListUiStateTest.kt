package com.maxkohne.fetchrewards.feature.items.list

import com.google.common.truth.Truth
import com.maxkohne.fetchrewards.data.items.Item
import org.junit.Test

internal class ItemsListUiStateTest {

    @Test
    fun `Item toUiItem`() {
        val item = Item(
            id = 1,
            listId = 2,
            name = "Item 1"
        )

        val result = item.toUiItem("Item 1")

        Truth.assertThat(result).isEqualTo(
            ItemsListUiState.UiItem(
                id = 1,
                listId = 2,
                name = "Item 1"
            )
        )
    }
}