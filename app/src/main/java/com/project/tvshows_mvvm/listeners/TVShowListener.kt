package com.project.tvshows_mvvm.listeners

import com.project.tvshows_mvvm.models.TVShow

interface TVShowListener {
    fun onTVShowClicked(tvShow: TVShow)
}