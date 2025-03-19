package com.maxkohne.fetchrewards.core.database.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.maxkohne.fetchrewards.core.database.FetchRewardsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {

    @Provides
    @Singleton
    fun providesRewardsDatabase(
        @ApplicationContext context: Context,
    ): FetchRewardsDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = FetchRewardsDatabase::class.java,
            name = "fetch_rewards_database",
        ).addCallback(object : RoomDatabase.Callback() {
            override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
                // TODO add analytics whenever this happens
            }
        }).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun providesItemsDao(database: FetchRewardsDatabase) = database.itemsDao()
}