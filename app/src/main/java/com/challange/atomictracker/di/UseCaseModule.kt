package com.challange.atomictracker.di

import com.challange.atomictracker.core.data.StockListRepository
import com.challange.atomictracker.core.domain.usecase.GetFeedStocksFlowUseCase
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
        repository: StockListRepository,
    ): GetFeedStocksFlowUseCase = GetFeedStocksFlowUseCase(repository)
}
