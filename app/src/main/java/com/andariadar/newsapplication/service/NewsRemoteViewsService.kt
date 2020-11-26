package com.andariadar.newsapplication.service

import android.content.Intent
import android.widget.RemoteViewsService
import com.andariadar.newsapplication.db.NewsDatabase
import com.andariadar.newsapplication.ui.adapter.NewsRemoteViewsFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Inject

@AndroidEntryPoint
class NewsRemoteViewsService: RemoteViewsService() {
    @Inject
    lateinit var database: NewsDatabase
    @InternalCoroutinesApi
    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory {

        return NewsRemoteViewsFactory(database, this, intent)
    }
}