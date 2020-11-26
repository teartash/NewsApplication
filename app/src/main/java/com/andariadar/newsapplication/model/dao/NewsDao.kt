package com.andariadar.newsapplication.model.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.andariadar.newsapplication.model.entity.News

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(items: List<News>)

    @Query("DELETE FROM news")
    fun clearAll()

    @Query("SELECT * FROM news")
    fun pagingSource(): PagingSource<Int, News>

    @Query("SELECT * FROM news LIMIT 5")
    fun allNews(): List<News>
}