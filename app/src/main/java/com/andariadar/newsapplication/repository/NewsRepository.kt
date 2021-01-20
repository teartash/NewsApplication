package com.andariadar.newsapplication.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.andariadar.newsapplication.data.RemoteMediator
import com.andariadar.newsapplication.db.NewsDatabase
import com.andariadar.newsapplication.api.NewsApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepository @Inject constructor(
    private val newsApi: NewsApi, private val database: NewsDatabase
) {

    companion object {
        private const val NETWORK_PAGE_SIZE = 20
    }

    @ExperimentalCoroutinesApi
    fun getNewsResults(period: Int) =
        Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            remoteMediator = RemoteMediator(
                period,
                database,
                newsApi
            ),
            pagingSourceFactory = { database.newsDao().pagingSource() }
        ).liveData
}