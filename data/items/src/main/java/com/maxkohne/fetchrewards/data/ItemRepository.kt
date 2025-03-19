package com.maxkohne.fetchrewards.data

import com.maxkohne.fetchrewards.core.database.items.ItemsDao
import com.maxkohne.fetchrewards.core.sync.common.SyncResult
import com.maxkohne.fetchrewards.core.sync.engine.SyncEngine
import com.maxkohne.fetchrewards.data.api.ItemsApi
import com.maxkohne.fetchrewards.data.mapper.toEntity
import com.maxkohne.fetchrewards.data.mapper.toItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface ItemRepository {
    suspend fun syncItems(): SyncResult
    fun getAllItemsFlow(): Flow<List<Item>>
    fun getItemFlow(id: Long): Flow<Item?>
}

internal class ItemRepositoryImpl @Inject constructor(
    private val api: ItemsApi,
    private val dao: ItemsDao,
    private val syncEngine: SyncEngine
) : ItemRepository {

    override fun getAllItemsFlow(): Flow<List<Item>> {
        return dao.getAllItemsFlow()
            .map { it.map { entity -> entity.toItem() } }
            .distinctUntilChanged()
    }

    override fun getItemFlow(id: Long): Flow<Item?> {
        return dao.getItemFlow(id)
            .map { it?.toItem() }
            .distinctUntilChanged()
    }

    override suspend fun syncItems(): SyncResult {
        return syncEngine.syncData(
            executeApiCall = api::getItems,
            saveResponse = { response -> dao.upsertAndDeleteItems(response.map { it.toEntity() }) }
        )
    }
}