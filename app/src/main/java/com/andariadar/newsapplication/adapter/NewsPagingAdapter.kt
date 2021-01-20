package com.andariadar.newsapplication.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.andariadar.newsapplication.databinding.ItemShortNewsBinding
import com.andariadar.newsapplication.model.News
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class NewsPagingAdapter(private val listener: OnItemClickListener):
    PagingDataAdapter<News, NewsPagingAdapter.NewsViewHolder>(
    NEWS_COMPARATOR) {

    inner class NewsViewHolder(private val binding: ItemShortNewsBinding): RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener{
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    if (item != null) {
                        listener.onItemClick(item)
                    }
                }
            }
        }

        fun bind(news: News) {
            binding.apply {

                if (news.media!!.isNotEmpty()) {
                    Log.i("newsMedia", news.mediaUrl)
                    val image = news.mediaUrl

                    Glide.with(itemView)
                        .load(image)
                        .centerCrop()
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(newsPhoto)

                } else {
                    newsPhoto.layoutParams.height = 0
                    Glide.with(itemView.context).clear(newsPhoto)
                    Log.i("newsMedia", "empty")
                }

                title.text = news.title
                section.text = news.section
                shortNews.text = news.shortNews
                publishDate.text = news.published_date
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(news: News) {

        }
    }

    companion object {
        val NEWS_COMPARATOR = object: DiffUtil.ItemCallback<News>() {
            override fun areItemsTheSame(oldItem: News, newItem: News) =
                oldItem.title == newItem.title

            override fun areContentsTheSame(oldItem: News, newItem: News) =
                oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val currentItem = getItem(position)

        if (currentItem != null) {
            holder.bind(currentItem)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = ItemShortNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return NewsViewHolder(binding)
    }
}