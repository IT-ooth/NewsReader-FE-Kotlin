package com.danyeon.newsreader.feature.news.domain.di

import com.danyeon.newsreader.core.network.api.NewsAPI
import com.danyeon.newsreader.feature.news.domain.repository.iface.NewsRepository
import com.danyeon.newsreader.feature.news.domain.repository.impl.NewsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideNewsRepository(api: NewsAPI): NewsRepository =
        NewsRepositoryImpl(api)
}