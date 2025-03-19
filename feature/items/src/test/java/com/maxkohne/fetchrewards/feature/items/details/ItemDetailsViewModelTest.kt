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

    private val mockRepository = mock<ItemRepository>()

    private lateinit var viewModel: ItemDetailsViewModel

    @Before
    fun setup() {
        viewModel = ItemDetailsViewModel(
            repository = mockRepository
        )
    }

    @Test
    fun `getItem success`() = runTest {
        val item = mock<Item>()
        mockRepository.stub {
            on { getItemFlow(1) } doReturn flowOf(item)
        }

        turbineScope {
            val eventFlow = viewModel.eventsFlow.testIn(backgroundScope)
            val uiStateFlow = viewModel.uiStateFlow.testIn(backgroundScope)

            Truth.assertThat(uiStateFlow.awaitItem()).isEqualTo(ItemsDetailsUiState())

            viewModel.getItem(1)

            Truth.assertThat(uiStateFlow.awaitItem()).isEqualTo(ItemsDetailsUiState().fromItem(item))
            eventFlow.expectNoEvents()
        }
    }

    @Test
    fun `getItem error`() = runTest {
        mockRepository.stub {
            on { getItemFlow(1) } doReturn flowOf(null)
        }

        turbineScope {
            val eventFlow = viewModel.eventsFlow.testIn(backgroundScope)
            val uiStateFlow = viewModel.uiStateFlow.testIn(backgroundScope)

            Truth.assertThat(uiStateFlow.awaitItem()).isEqualTo(ItemsDetailsUiState())

            viewModel.getItem(1)

            Truth.assertThat(eventFlow.awaitItem()).isEqualTo(ItemDetailsEvent.NotFound)
            uiStateFlow.expectNoEvents()
        }
    }
}