package com.project.tvshows_mvvm.responses

import com.google.gson.annotations.SerializedName
import com.project.tvshows_mvvm.models.TVShow

class TVShowsResponse {

    @SerializedName("page")
    val page: Int? = null

    @SerializedName("pages")
    val totalPages: Int? = null

    @SerializedName("tv_shows")
    val tvShows: ArrayList<TVShow>? = null

}