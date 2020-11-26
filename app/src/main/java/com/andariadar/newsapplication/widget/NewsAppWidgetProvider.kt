package com.andariadar.newsapplication.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.andariadar.newsapplication.R
import com.andariadar.newsapplication.service.NewsRemoteViewsService
import com.andariadar.newsapplication.ui.activity.MainActivity


const val ACTION_WEBVIEW = "com.andariadar.newsapplication.actionToast"
const val EXTRA_ITEM_URL = "com.andariadar.newsapplication.extraItemPosition"

class NewsAppWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        appWidgetIds.forEach { appWidgetId ->

            val clickIntent = Intent(context, NewsAppWidgetProvider::class.java)
            clickIntent.action = ACTION_WEBVIEW
            val clickPendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                clickIntent,
                0
            )

            val serviceIntent = Intent(context, NewsRemoteViewsService::class.java)

            val remoteViews = RemoteViews(context.packageName, R.layout.news_app_widget_provider).apply {

                setRemoteAdapter(R.id.list_view, serviceIntent)

                setPendingIntentTemplate(R.id.list_view, clickPendingIntent)

                setOnClickPendingIntent(
                    R.id.btn_start,
                    getPendingIntent(context)
                )
            }

            appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
        }

        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.list_view)

        super.onUpdate(context, appWidgetManager, appWidgetIds)
    }

    private fun getPendingIntent(context: Context): PendingIntent
    {
        val intent = Intent(context, MainActivity::class.java)
        return PendingIntent.getActivity(context, 0, intent, 0)
    }

    override fun onReceive(context: Context?, intent: Intent?) {
       if(ACTION_WEBVIEW == intent!!.action) {
           val clickedUrl = intent.getStringExtra(EXTRA_ITEM_URL)
           try {
               val configIntent = Intent(context, MainActivity::class.java)
               configIntent.putExtra("key", true)
               configIntent.putExtra("url", clickedUrl)
               configIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
               context!!.startActivity(configIntent)

           } catch (e: RuntimeException) {
               e.printStackTrace()
           }
        }

        super.onReceive(context, intent)
    }
}

