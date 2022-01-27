package com.project.tvshows_mvvm.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.project.tvshows_mvvm.repositories.SearchTVShowRepository
import com.project.tvshows_mvvm.responses.TVShowsResponse

class SearchViewModel : ViewModel() {

    private val searchTVShowRepository: SearchTVShowRepository = SearchTVShowRepository()

    fun searchTVShow(query: String, page: Int): LiveData<TVShowsResponse> {
        return searchTVShowRepository.searchTVShow(query, page)
    }
}