package com.maxkohne.fetchrewards.core.database.items

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth
import com.maxkohne.fetchrewards.core.database.FetchRewardsDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
internal class ItemsDaoTest {

    private lateinit var itemsDao: ItemsDao
    private lateinit var database: FetchRewardsDatabase

    @Before
    fun createDb() {
        database = Room.inMemoryDatabaseBuilder(
            context = ApplicationProvider.getApplicationContext(),
            klass = FetchRewardsDatabase::class.java
        ).build()
        itemsDao = database.itemsDao()
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun upsertAndReadItems() = runTest {
        val itemEntity = ItemEntity(
            id = 1,
            listId = 1,
            name = "Item 1"
        )

        itemsDao.upsertItems(listOf(itemEntity))
        val items = itemsDao.getAllItemsFlow().first()

        Truth.assertThat(items).containsExactly(itemEntity)
    }
}