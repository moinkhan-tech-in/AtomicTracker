package com.challange.atomictracker.di

import com.challange.atomictracker.data.DefaultStockRepository
import com.challange.atomictracker.domain.repository.StockRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindStockListRepository(impl: DefaultStockRepository): StockRepository
}
