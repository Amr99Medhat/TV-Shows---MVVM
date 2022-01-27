package com.project.tvshows_mvvm.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.project.tvshows_mvvm.adapters.TVShowsAdapter
import com.project.tvshows_mvvm.databinding.ActivityMainBinding
import com.project.tvshows_mvvm.listeners.TVShowListener
import com.project.tvshows_mvvm.models.TVShow
import com.project.tvshows_mvvm.viewModels.MostPopularTVShowsViewModel
import java.util.ArrayList

class MainActivity : AppCompatActivity(), TVShowListener {
    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var mostPopularTVShowsViewModel: MostPopularTVShowsViewModel
    private val tvShows: ArrayList<TVShow> = ArrayList()
    private lateinit var tVShowsAdapter: TVShowsAdapter
    private var currentPage: Int = 1
    private var totalAvailablePages: Int = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)
        doInitialization()

    }

    private fun doInitialization() {
        activityMainBinding.txShowsRecyclerView.setHasFixedSize(true)
        mostPopularTVShowsViewModel =
            ViewModelProvider(this).get(MostPopularTVShowsViewModel::class.java)
        tVShowsAdapter = TVShowsAdapter(tvShows, this)
        activityMainBinding.txShowsRecyclerView.adapter = tVShowsAdapter
        activityMainBinding.txShowsRecyclerView.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!activityMainBinding.txShowsRecyclerView.canScrollVertically(1)) {
                    if (currentPage <= totalAvailablePages) {
                        currentPage += 1
                        getMostPopularTVShows()
                    }
                }
            }
        })
        activityMainBinding.imageWatchList.setOnClickListener {
            startActivity(Intent(this, WatchlistActivity::class.java))
        }
        activityMainBinding.imageSearch.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }
        getMostPopularTVShows()

    }

    private fun getMostPopularTVShows() {
        toggleLoading()
        mostPopularTVShowsViewModel.getMostPopularTVShows(currentPage)
            .observe(this, { mostPopularTVShowsResponse ->
                toggleLoading()
                if (mostPopularTVShowsResponse != null) {
                    totalAvailablePages = mostPopularTVShowsResponse.totalPages!!
                    if (mostPopularTVShowsResponse.tvShows != null) {
                        val oldCount = tvShows.size
                        tvShows.addAll(mostPopularTVShowsResponse.tvShows)
                        tVShowsAdapter.notifyItemRangeInserted(oldCount, tvShows.size)
                    }
                }
            })
    }


    private fun toggleLoading() {
        if (currentPage == 1) {
            activityMainBinding.isLoading =
                !(activityMainBinding.isLoading != null && activityMainBinding.isLoading == true)
        } else {
            activityMainBinding.isLoadingMore =
                !(activityMainBinding.isLoadingMore != null && activityMainBinding.isLoadingMore == true)
        }
    }

    override fun onTVShowClicked(tvShow: TVShow) {
        val intent = Intent(applicationContext, TVShowDetailsActivity::class.java)
        intent.putExtra("tvShow", tvShow)
        startActivity(intent)
    }
}