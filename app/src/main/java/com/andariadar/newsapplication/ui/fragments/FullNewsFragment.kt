package com.andariadar.newsapplication.ui.fragments

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.andariadar.newsapplication.R
import com.andariadar.newsapplication.databinding.FragmentFullNewsBinding
import com.andariadar.newsapplication.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_full_news.*

@AndroidEntryPoint
class FullNewsFragment: Fragment(R.layout.fragment_full_news) {
    private val viewModel by viewModels<NewsViewModel>()

    //private val args by navArgs<FullNewsFragmentArgs>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentFullNewsBinding.bind(view)

        binding.apply {
            /*val url = args.url

            //fullNews.loadUrl(news.url)
            full_news.loadUrl(url)*/

            full_news.loadUrl(arguments?.getString("url"))
        }
    }
}