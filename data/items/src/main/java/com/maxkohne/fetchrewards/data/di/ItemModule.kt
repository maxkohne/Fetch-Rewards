package com.maxkohne.fetchrewards.data.di

import com.maxkohne.fetchrewards.data.ItemRepository
import com.maxkohne.fetchrewards.data.ItemRepositoryImpl
import com.maxkohne.fetchrewards.data.api.ItemsApi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class ItemModule {

    companion object {
        @Provides
        @Singleton
        fun providesFetchApi(retrofit: Retrofit): ItemsApi {
            return retrofit.create(ItemsApi::class.java)
        }
    }

    @Binds
    abstract fun bindsFetchItemRepository(impl: ItemRepositoryImpl): ItemRepository
}