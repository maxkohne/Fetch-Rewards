package com.maxkohne.fetchrewards.feature.items.details

import app.cash.turbine.turbineScope
import com.google.common.truth.Truth
import com.maxkohne.fetchrewards.data.items.Item
import com.maxkohne.fetchrewards.data.items.ItemRepository
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.stub

internal class ItemDetailsViewModelTest {

    companion object {
        private const val ITEM_ID = 1L
    }

    private val mockRepository = mock<ItemRepository>()

    private lateinit var viewModel: ItemDetailsViewModel

    @Before
    fun setup() {
        mockRepository.stub {
            on { getItemFlow(ITEM_ID) } doReturn flowOf(null)
        }
        viewModel = ItemDetailsViewModel(
            repository = mockRepository,
            itemId = ITEM_ID
        )
    }

    @Test
    fun `getItem success`() = runTest {
        val item = mock<Item>()
        mockRepository.stub {
            on { getItemFlow(ITEM_ID) } doReturn flowOf(item)
        }

        turbineScope {
            val uiStateFlow = viewModel.uiStateFlow.testIn(backgroundScope)
            val eventFlow = viewModel.eventsFlow.testIn(backgroundScope)

            // Consume initial events
            eventFlow.awaitItem()
            uiStateFlow.awaitItem()

            viewModel.getItem(ITEM_ID)

            Truth.assertThat(uiStateFlow.awaitItem()).isEqualTo(ItemsDetailsUiState().fromItem(item))
            uiStateFlow.expectNoEvents()
        }
    }

    @Test
    fun `getItem error`() = runTest {
        mockRepository.stub {
            on { getItemFlow(ITEM_ID) } doReturn flowOf(null)
        }

        turbineScope {
            val eventFlow = viewModel.eventsFlow.testIn(backgroundScope)
            val uiStateFlow = viewModel.uiStateFlow.testIn(backgroundScope)

            // Consume initial events
            eventFlow.awaitItem()
            uiStateFlow.awaitItem()

            viewModel.getItem(ITEM_ID)

            Truth.assertThat(eventFlow.awaitItem()).isEqualTo(ItemDetailsEvent.NotFound)
            uiStateFlow.expectNoEvents()
        }
    }
}