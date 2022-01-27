package com.project.tvshows_mvvm.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.project.tvshows_mvvm.adapters.TVShowsAdapter
import com.project.tvshows_mvvm.databinding.ActivitySearchBinding
import com.project.tvshows_mvvm.listeners.TVShowListener
import com.project.tvshows_mvvm.models.TVShow
import com.project.tvshows_mvvm.viewModels.SearchViewModel
import java.util.*
import kotlin.collections.ArrayList

class SearchActivity : AppCompatActivity(), TVShowListener {
    private lateinit var activitySearchBinding: ActivitySearchBinding
    private lateinit var viewModel: SearchViewModel
    private val tvShows: ArrayList<TVShow> = ArrayList()
    private lateinit var tVShowsAdapter: TVShowsAdapter
    private var currentPage: Int = 1
    private var totalAvailablePages: Int = 1
    private var timer: Timer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySearchBinding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(activitySearchBinding.root)
        doInitialization()
    }

    private fun doInitialization() {
        activitySearchBinding.imageBack.setOnClickListener {
            onBackPressed()
        }
        activitySearchBinding.tvShowsRecyclerView.setHasFixedSize(true)
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        tVShowsAdapter = TVShowsAdapter(tvShows, this)
        activitySearchBinding.tvShowsRecyclerView.adapter = tVShowsAdapter
        activitySearchBinding.inputSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                timer?.cancel()
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().trim().isNotEmpty()) {
                    timer = Timer()
                    timer!!.schedule(object : TimerTask() {
                        override fun run() {
                            Handler(Looper.getMainLooper()).post {
                                currentPage = 1
                                totalAvailablePages = 1
                                searchTVShow(s.toString())
                            }
                        }
                    }, 800)
                } else {
                    tvShows.clear()
                    tVShowsAdapter.notifyDataSetChanged()
                }
            }
        })
        activitySearchBinding.tvShowsRecyclerView.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!activitySearchBinding.tvShowsRecyclerView.canScrollVertically(1)) {
                    if (activitySearchBinding.inputSearch.text.toString().isNotEmpty()) {
                        if (currentPage < totalAvailablePages) {
                            currentPage += 1
                            searchTVShow(activitySearchBinding.inputSearch.text.toString())
                        }
                    }
                }

            }
        })
        activitySearchBinding.inputSearch.requestFocus()

    }

    private fun searchTVShow(query: String) {
        toggleLoading()
        viewModel.searchTVShow(query, currentPage).observe(this, { tVShowsResponse ->
            toggleLoading()
            if (tVShowsResponse != null) {
                totalAvailablePages = tVShowsResponse.totalPages!!
                if (tVShowsResponse.tvShows != null) {
                    val oldCount = tvShows.size
                    tvShows.addAll(tVShowsResponse.tvShows)
                    tVShowsAdapter.notifyItemRangeInserted(oldCount, tvShows.size)
                }
            }
        })
    }

    private fun toggleLoading() {
        if (currentPage == 1) {
            activitySearchBinding.isLoading =
                !(activitySearchBinding.isLoading != null && activitySearchBinding.isLoading == true)
        } else {
            activitySearchBinding.isLoadingMore =
                !(activitySearchBinding.isLoadingMore != null && activitySearchBinding.isLoadingMore == true)
        }
    }

    override fun onTVShowClicked(tvShow: TVShow) {
        val intent = Intent(applicationContext, TVShowDetailsActivity::class.java)
        intent.putExtra("tvShow", tvShow)
        startActivity(intent)
    }
}