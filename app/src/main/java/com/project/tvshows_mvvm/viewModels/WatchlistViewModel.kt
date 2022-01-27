package com.project.tvshows_mvvm.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.project.tvshows_mvvm.database.TVShowsDatabase
import com.project.tvshows_mvvm.models.TVShow
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

class WatchlistViewModel(application: Application) : AndroidViewModel(application) {

    private val tvShowsDatabase: TVShowsDatabase = TVShowsDatabase.getTVShowsDatabase(application)


    fun loadWatchlist(): Flowable<List<TVShow>> {
        return tvShowsDatabase.tvShowDao().getWatchlist()
    }

    fun removeTVShowFromWatchlist(tvShow: TVShow): Completable {
        return tvShowsDatabase.tvShowDao().removeFromWatchlist(tvShow)
    }


}