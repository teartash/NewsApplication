package com.andariadar.newsapplication.ui

import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.andariadar.newsapplication.repository.NewsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi


class NewsViewModel @ViewModelInject constructor(
    private val repository: NewsRepository,
    @Assisted state: SavedStateHandle
): ViewModel() {

    private val currentPeriod = state.getLiveData(CURRENT_PERIOD, DEFAULT_PERIOD)

    @ExperimentalCoroutinesApi
    val news = currentPeriod.switchMap { queryString ->
        repository.getNewsResults(queryString).cachedIn(viewModelScope)
    }

    fun getNewsByPeriod(period: Int) {
        currentPeriod.value = period
        Log.i("currentQuery", period.toString())
    }

    companion object {
        private const val CURRENT_PERIOD = "current_query"
        private const val DEFAULT_PERIOD = 1
    }
}