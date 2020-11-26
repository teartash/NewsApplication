package com.andariadar.newsapplication.ui.fragments

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.andariadar.newsapplication.R
import com.andariadar.newsapplication.databinding.FragmentNewsBinding
import com.andariadar.newsapplication.model.entity.News
import com.andariadar.newsapplication.ui.adapter.NewsPagingAdapter
import com.andariadar.newsapplication.utils.NetworkUtils
import com.andariadar.newsapplication.utils.getColorRes
import com.andariadar.newsapplication.utils.hide
import com.andariadar.newsapplication.utils.show
import com.andariadar.newsapplication.viewmodel.NewsViewModel
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi


const val ANIMATION_DURATION = 1000.toLong()

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class NewsFragment: Fragment(R.layout.fragment_news), NewsPagingAdapter.OnItemClickListener {

    private val viewModel by viewModels<NewsViewModel>()

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: NewsPagingAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = NewsPagingAdapter(this)
        binding.apply {
            recyclerView.setHasFixedSize(true)
            recyclerView.adapter = adapter
            recyclerView.itemAnimator = null

            adapter.registerAdapterDataObserver(object: RecyclerView.AdapterDataObserver() {
                override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                    if (positionStart == 0) {
                        recyclerView.scrollToPosition(0)
                    }
                }
            })
        }

        viewModel.news.observe(viewLifecycleOwner) {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        adapter.addLoadStateListener { loadState ->
            binding.apply {
                Log.i("lState", loadState.source.refresh.toString())
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
            }
        }

        handleNetworkChanges()
    }

    private fun handleNetworkChanges()
    {
        NetworkUtils.getNetworkLiveData(requireContext()).observe(viewLifecycleOwner) {
            if (!it)
            {
                binding.textViewNetworkStatus.text = getString(R.string.text_no_connectivity)
                binding.networkStatusLayout.apply {
                    show()
                    setBackgroundColor(getColorRes(R.color.colorStatusNotConnected))
                }
                binding.selectPeriodLayout.hide()

            }
            else
            {
                adapter.retry()

                binding.textViewNetworkStatus.text = getString(R.string.text_connectivity)
                binding.networkStatusLayout.setBackgroundColor(getColorRes(R.color.colorStatusConnected))
                doAnimation(binding.networkStatusLayout)
                binding.selectPeriodLayout.show()

                binding.chipGroup.setOnCheckedChangeListener { chipGroup, i ->
                    val chip: Chip? = chipGroup.findViewById(i)
                    if (chip != null)
                        viewModel.getNewsByPeriod(selectPeriod(chip.text as String))
                }
            }
        }
    }

    private fun doAnimation(linearLayout: LinearLayout)
    {
        linearLayout.apply {
            animate()
                .alpha(1f)
                .setStartDelay(ANIMATION_DURATION)
                .setDuration(ANIMATION_DURATION)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        hide()
                    }
                })
        }
    }

    override fun onItemClick(news: News) {
        val bundle = bundleOf("url" to news.url)
        findNavController().navigate(R.id.action_newsFragment_to_fullNewsFragment, bundle)
        /*val action = NewsFragmentDirections.actionNewsFragmentToFullNewsFragment(news.url)
        findNavController().navigate(action)*/
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun selectPeriod(label: String): Int {
        return when(label) {
            "day" -> 1
            "week" -> 7
            "month" -> 30
            else -> 1
        }
    }
}