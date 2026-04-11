package com.challange.atomictracker.di

import com.challange.atomictracker.core.data.StockRepository
import com.challange.atomictracker.core.domain.usecase.GetFeedStocksFlowUseCase
import com.challange.atomictracker.core.domain.usecase.GetStockSymbolFlowUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideGetFeedStocksFlowUseCase(
        repository: StockRepository,
    ): GetFeedStocksFlowUseCase = GetFeedStocksFlowUseCase(repository)

    @Provides
    @Singleton
    fun provideObserveStockFlowUseCase(
        repository: StockRepository,
    ): GetStockSymbolFlowUseCase = GetStockSymbolFlowUseCase(repository)
}
