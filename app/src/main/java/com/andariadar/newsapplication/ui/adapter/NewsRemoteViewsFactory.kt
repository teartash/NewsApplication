package com.andariadar.newsapplication.ui.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.SystemClock
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.andariadar.newsapplication.R
import com.andariadar.newsapplication.db.NewsDatabase
import com.andariadar.newsapplication.model.entity.News
import com.andariadar.newsapplication.widget.EXTRA_ITEM_URL
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import kotlinx.coroutines.ExperimentalCoroutinesApi


class NewsRemoteViewsFactory(
    private val database: NewsDatabase,
    private val context: Context,
    intent: Intent?
) : RemoteViewsService.RemoteViewsFactory {

    private var listNews: List<News> = listOf()

    override fun onCreate() {}

    override fun onDataSetChanged() {
        listNews = database.newsDao().allNews()
    }

    override fun onDestroy() {}

    override fun getCount(): Int {
        return listNews.size
    }

    @ExperimentalCoroutinesApi
    override fun getViewAt(position: Int): RemoteViews {

        val row = RemoteViews(context.packageName, R.layout.item_widget)

        row.setTextViewText(R.id.title_widget, listNews[position].title)
        row.setTextViewText(R.id.byline_widget, listNews[position].byline)
        row.setTextViewText(R.id.published_date_widget, listNews[position].published_date)

        if (listNews[position].media!!.isNotEmpty()) {
            try {
                //val bitmap: Bitmap =
                    Glide.with(context)
                    .asBitmap()
                    .load(listNews[position].mediaUrl)
                    .listener(object: RequestListener<Bitmap> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Bitmap>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            return false
                        }

                        override fun onResourceReady(
                            resource: Bitmap?,
                            model: Any?,
                            target: Target<Bitmap>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            row.setImageViewBitmap(R.id.image_widget, resource)
                            return false
                        }
                    })
                    .submit(110, 90)
                    .get()

            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            row.setImageViewResource(R.id.image_widget, R.drawable.ic_ny)
        }

        val fillIntent = Intent()
        fillIntent.putExtra(EXTRA_ITEM_URL, listNews[position].url)
        row.setOnClickFillInIntent(R.id.title_widget, fillIntent)

        SystemClock.sleep(500)

        return row
    }

    override fun getLoadingView(): RemoteViews? {
        return null
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }
}