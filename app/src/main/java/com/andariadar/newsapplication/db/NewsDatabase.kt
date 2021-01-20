package com.andariadar.newsapplication.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.andariadar.newsapplication.model.News

@Database(entities = [News::class], version = 1)
abstract class NewsDatabase: RoomDatabase() {
    abstract fun newsDao(): NewsDao
}