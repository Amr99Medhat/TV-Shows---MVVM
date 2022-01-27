package com.project.tvshows_mvvm.listeners

import com.project.tvshows_mvvm.models.TVShow

interface WatchlistListener {

    fun onTVShowClicked(tvShow: TVShow)

    fun removeTVShowFromWatchlist(tvShow: TVShow, position: Int)
}