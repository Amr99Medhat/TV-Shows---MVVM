package com.project.tvshows_mvvm.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.project.tvshows_mvvm.repositories.MostPopularTVShowsRepository
import com.project.tvshows_mvvm.responses.TVShowsResponse



class MostPopularTVShowsViewModel : ViewModel() {

    private var mostPopularTVShowsRepository: MostPopularTVShowsRepository =
        MostPopularTVShowsRepository()

    fun getMostPopularTVShows(page: Int): LiveData<TVShowsResponse> {
        return mostPopularTVShowsRepository.getMostPopularTVShows(page)
    }
}