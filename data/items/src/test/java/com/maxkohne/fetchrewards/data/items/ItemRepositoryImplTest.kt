package com.maxkohne.fetchrewards.data.items

import com.google.common.truth.Truth
import com.maxkohne.fetchrewards.core.database.items.ItemsDao
import com.maxkohne.fetchrewards.core.sync.common.SyncResult
import com.maxkohne.fetchrewards.core.sync.engine.SyncEngine
import com.maxkohne.fetchrewards.data.items.api.ItemResponse
import com.maxkohne.fetchrewards.data.items.api.ItemsApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.anyList
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.doSuspendableAnswer
import org.mockito.kotlin.stub
import org.mockito.kotlin.verify

@RunWith(MockitoJUnitRunner::class)
internal class ItemRepositoryImplTest {

    // Mocks
    private val mockApi = mock<ItemsApi>()
    private val mockDao = mock<ItemsDao>()
    private val mockSyncEngine = mock<SyncEngine>()

    // System under test
    private lateinit var repository: ItemRepositoryImpl

    @Before
    fun setup() {
        StandardTestDispatcher()
        repository = ItemRepositoryImpl(
            api = mockApi,
            dao = mockDao,
            syncEngine = mockSyncEngine
        )
    }

    @Test
    fun `getAllItemsFlow should return mapped items from dao`() = runTest {
        mockDao.stub {
            on { getAllItemsFlow() } doReturn flowOf(listOf(itemEntity1, itemEntity2))
        }

        val result = repository.getAllItemsFlow().first()

        Truth.assertThat(result).isEqualTo(listOf(item1, item2))
    }

    @Test
    fun `getItemFlow should return mapped item from dao`() = runTest {
        val id = itemEntity1.id

        mockDao.stub {
            on { getItemFlow(id) } doReturn flowOf(itemEntity1)
        }

        val result = repository.getItemFlow(id).first()

        Truth.assertThat(result).isEqualTo(item1)
    }

    @Test
    fun `getItemFlow should return null when dao has no item`() = runTest {
        val id = itemEntity1.id

        mockDao.stub {
            on { getItemFlow(id) } doReturn flowOf(null)
        }

        val result = repository.getItemFlow(id).first()

        Truth.assertThat(result).isNull()
    }

    @Test
    fun `syncItems should call syncEngine with correct params`() = runTest {
        mockSyncEngine.stub {
            onBlocking {
                syncData<List<ItemResponse>>(
                    executeApiCall = any(),
                    saveResponse = any()
                )
            } doSuspendableAnswer  {
                val executeApiCall = it.getArgument<suspend () -> List<ItemResponse>>(0)
                val saveResponse = it.getArgument<suspend (List<ItemResponse>) -> Unit>(1)

                executeApiCall.invoke()
                saveResponse.invoke(anyList())

                SyncResult.Success
            }
        }

        val result = repository.syncItems()

        verify(mockApi).getItems()
        verify(mockDao).upsertAndDeleteItems(any())
        Truth.assertThat(result).isEqualTo(SyncResult.Success)
    }
}