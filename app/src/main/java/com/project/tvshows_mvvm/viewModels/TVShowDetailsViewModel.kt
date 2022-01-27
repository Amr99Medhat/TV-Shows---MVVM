package com.project.tvshows_mvvm.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.project.tvshows_mvvm.database.TVShowsDatabase
import com.project.tvshows_mvvm.models.TVShow
import com.project.tvshows_mvvm.repositories.TVShowDetailsRepository
import com.project.tvshows_mvvm.responses.TVShowDetailsResponse
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

class TVShowDetailsViewModel(application: Application?) : AndroidViewModel(application!!) {

    private var tVShowDetailsRepository: TVShowDetailsRepository =
        TVShowDetailsRepository()
    private var tvShowsDatabase: TVShowsDatabase = TVShowsDatabase.getTVShowsDatabase(application!!)


    fun getTVShowDetails(tvShowId: String): LiveData<TVShowDetailsResponse> {
        return tVShowDetailsRepository.getTVShowDerailsRepository(tvShowId)
    }

    fun addToWatchlist(tvShow: TVShow): Completable {
        return tvShowsDatabase.tvShowDao().addToWatchlist(tvShow)
    }

    fun getTVShowFromWatchlist(tvShowId: String): Flowable<TVShow> {
        return tvShowsDatabase.tvShowDao().getTVShowFromWatchlist(tvShowId)
    }

    fun removeTVShowFromWatchlist(tvShow : TVShow) : Completable{
        return tvShowsDatabase.tvShowDao().removeFromWatchlist(tvShow)
    }
}