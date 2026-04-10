package com.challange.atomictracker.di

import com.challange.atomictracker.core.data.datasource.NetworkStocksDataSource
import com.challange.atomictracker.core.data.datasource.StocksDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindStockListDataSource(impl: NetworkStocksDataSource): StocksDataSource
}
