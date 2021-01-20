package com.andariadar.newsapplication.data

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.andariadar.newsapplication.db.NewsDatabase
import com.andariadar.newsapplication.api.NewsApi
import com.andariadar.newsapplication.model.News
import androidx.room.withTransaction
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class RemoteMediator(
    private val period: Int,
    private val database: NewsDatabase,
    private val newsApi: NewsApi
): RemoteMediator<Int, News>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, News>
    ): MediatorResult {
        return try {

            val loadKey = when (loadType) {
                LoadType.REFRESH -> null

                LoadType.PREPEND ->
                    return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = true
                        )
                    lastItem.id
                }
            }

            val response = newsApi.newsOneDay(period)

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.newsDao().clearAll()
                    database.newsDao().insertAll(response.results)
                }
            }
            MediatorResult.Success(
                endOfPaginationReached = false
            )
        }
        catch (e: IOException) {
            Log.i("IOException", e.toString())
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            Log.i("HttpException", e.toString())
            MediatorResult.Error(e)
        }
    }
}
