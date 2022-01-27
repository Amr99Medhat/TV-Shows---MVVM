package com.project.tvshows_mvvm.network

import com.project.tvshows_mvvm.responses.TVShowDetailsResponse
import com.project.tvshows_mvvm.responses.TVShowsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("most-popular")
    fun getMostPopularShows(@Query("page") page: Int): Call<TVShowsResponse>


    @GET("show-details")
    fun getTVShowDetails(@Query("q") tvShowId: String): Call<TVShowDetailsResponse>

    @GET("search")
    fun searchTVShow(@Query("q") query: String, @Query("page") page: Int): Call<TVShowsResponse>

}