package com.maxkohne.fetchrewards.core.database.items

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ItemsDao {

    @Query("SELECT * FROM items")
    abstract fun getAllItemsFlow(): Flow<List<ItemEntity>>

    @Query("SELECT * FROM items WHERE id = :id")
    abstract fun getItemFlow(id: Long): Flow<ItemEntity?>

    @Upsert
    abstract suspend fun upsertItems(items: List<ItemEntity>)

    @Query("SELECT id FROM items")
    abstract suspend fun getAllIds() : List<Long>

    @Query("DELETE FROM items WHERE id in (:ids)")
    abstract suspend fun deleteByIds(ids: Set<Long>)

    @Transaction
    open suspend fun upsertAndDeleteItems(items: List<ItemEntity>) {
        // Prune items that exist in cache but were deleted on the server
        val idsFromResponse = items.asSequence().map { it.id }.toSet()
        val idsFromStorage = getAllIds().toSet()
        deleteByIds(idsFromStorage - idsFromResponse)

        // Upsert items
        upsertItems(items)
    }
}