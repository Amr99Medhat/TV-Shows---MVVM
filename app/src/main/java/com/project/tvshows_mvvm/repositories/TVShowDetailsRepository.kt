package com.project.tvshows_mvvm.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.project.tvshows_mvvm.network.ApiClient
import com.project.tvshows_mvvm.network.ApiService
import com.project.tvshows_mvvm.responses.TVShowDetailsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TVShowDetailsRepository {

    private var apiService: ApiService = ApiClient.getRetrofit()!!.create(ApiService::class.java)

    fun getTVShowDerailsRepository(tvShowId: String): LiveData<TVShowDetailsResponse> {

        val data: MutableLiveData<TVShowDetailsResponse> = MutableLiveData()

        apiService.getTVShowDetails(tvShowId).enqueue(object : Callback<TVShowDetailsResponse> {
            override fun onResponse(
                call: Call<TVShowDetailsResponse>,
                response: Response<TVShowDetailsResponse>
            ) {
                data.value = response.body()
            }

            override fun onFailure(call: Call<TVShowDetailsResponse>, t: Throwable) {
                data.value = null
            }

        })
        return data
    }
}