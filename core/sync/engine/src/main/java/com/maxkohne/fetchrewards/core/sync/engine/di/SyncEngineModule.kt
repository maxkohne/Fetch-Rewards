package com.maxkohne.fetchrewards.core.sync.engine.di

import com.maxkohne.fetchrewards.core.sync.engine.SyncEngine
import com.maxkohne.fetchrewards.core.sync.engine.SyncEngineImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class SyncEngineModule {
    @Binds
    @Singleton
    abstract fun bindsSyncEngine(impl: SyncEngineImpl): SyncEngine
}