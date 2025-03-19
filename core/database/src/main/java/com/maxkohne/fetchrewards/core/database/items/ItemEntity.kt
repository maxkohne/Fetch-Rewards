package com.maxkohne.fetchrewards.core.database.items

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class ItemEntity(
    @PrimaryKey(autoGenerate = false) @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "list_id") val listId: Long,
    @ColumnInfo(name = "name") val name: String?,
)