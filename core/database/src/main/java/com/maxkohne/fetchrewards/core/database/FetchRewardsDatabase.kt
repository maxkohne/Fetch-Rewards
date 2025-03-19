package com.maxkohne.fetchrewards.core.database

import androidx.room.Database
import androidx.room.RewriteQueriesToDropUnusedColumns
import androidx.room.RoomDatabase
import com.maxkohne.fetchrewards.core.database.items.ItemEntity
import com.maxkohne.fetchrewards.core.database.items.ItemsDao

@Database(
    exportSchema = false, // TODO set to true for AutoMigrations. false for now to avoid commiting json files
    entities = [
        ItemEntity::class
    ],
    autoMigrations = [],
    version = 1
)
@RewriteQueriesToDropUnusedColumns
internal abstract class FetchRewardsDatabase : RoomDatabase() {
    abstract fun itemsDao(): ItemsDao
}