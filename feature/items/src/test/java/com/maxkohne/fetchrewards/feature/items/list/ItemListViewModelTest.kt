package com.maxkohne.fetchrewards.feature.items.list

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.turbineScope
import com.google.common.truth.Truth
import com.maxkohne.fetchrewards.core.sync.common.SyncError
import com.maxkohne.fetchrewards.core.sync.common.SyncResult
import com.maxkohne.fetchrewards.data.items.ItemRepository
import com.maxkohne.fetchrewards.feature.items.list.usecase.GetItemsListSectionsUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.stub

internal class ItemListViewModelTest {

    private val mockItemRepository = mock<ItemRepository>()
    private val mockSavedStateHandle = mock<SavedStateHandle>()
    private val mockGetItemsListSectionsUseCase = mock<GetItemsListSectionsUseCase>()

    private lateinit var viewModel: ItemListViewModel

    @Before
    fun setup() {
        viewModel = ItemListViewModel(
            itemRepository = mockItemRepository,
            savedStateHandle = mockSavedStateHandle,
            getItemsListSectionsUseCase = mockGetItemsListSectionsUseCase
        )
    }

    @Test
    fun searchItems() {
        val query = "Test Query"

        viewModel.searchItems(query)

        verify(mockSavedStateHandle)["search_query"] = query
    }

    @Test
    fun `syncItems success`() = runTest {
        mockItemRepository.stub {
            onBlocking { syncItems() } doReturn SyncResult.Success
        }

        turbineScope {
            val uiStateFlow = viewModel.uiStateFlow.testIn(backgroundScope)
            Truth.assertThat(uiStateFlow.awaitItem().isLoading).isFalse()

            viewModel.syncItems()

            Truth.assertThat(uiStateFlow.awaitItem().isLoading).isTrue()
            Truth.assertThat(uiStateFlow.awaitItem().isLoading).isFalse()
            uiStateFlow.ensureAllEventsConsumed()
        }
    }

    @Test
    fun `syncItems error`() = runTest {
        val errorMap = mapOf(
            SyncError.General to ItemsListEvent.SyncError.General,
            SyncError.ServerDown to ItemsListEvent.SyncError.ServerDown,
            SyncError.ConnectivityError to ItemsListEvent.SyncError.ConnectionError,
            SyncError.Forbidden to ItemsListEvent.SyncError.Forbidden,
            SyncError.StorageFull to ItemsListEvent.SyncError.StorageFull
        )
        turbineScope {
            val uiStateFlow = viewModel.uiStateFlow.testIn(backgroundScope)
            val eventFlow = viewModel.eventsFlow.testIn(backgroundScope)

            Truth.assertThat(uiStateFlow.awaitItem().isLoading).isFalse()

            errorMap.forEach { (syncError, event) ->
                mockItemRepository.stub {
                    onBlocking { syncItems() } doReturn SyncResult.Failure(error = syncError)
                }

                viewModel.syncItems()

                Truth.assertThat(uiStateFlow.awaitItem().isLoading).isTrue()
                Truth.assertThat(uiStateFlow.awaitItem().isLoading).isFalse()
                uiStateFlow.ensureAllEventsConsumed()

                Truth.assertThat(eventFlow.awaitItem()).isEqualTo(event)
                eventFlow.ensureAllEventsConsumed()
            }
        }
    }
}