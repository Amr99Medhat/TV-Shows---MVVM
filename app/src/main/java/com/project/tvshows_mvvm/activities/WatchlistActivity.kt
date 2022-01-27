package com.project.tvshows_mvvm.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.project.tvshows_mvvm.adapters.WatchlistAdapter
import com.project.tvshows_mvvm.databinding.ActivityWatchlistBinding
import com.project.tvshows_mvvm.listeners.WatchlistListener
import com.project.tvshows_mvvm.models.TVShow
import com.project.tvshows_mvvm.utlities.TempDataHolder
import com.project.tvshows_mvvm.viewModels.WatchlistViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class WatchlistActivity : AppCompatActivity(), WatchlistListener {

    private lateinit var activityWatchlistBinding: ActivityWatchlistBinding
    private lateinit var viewModel: WatchlistViewModel
    private lateinit var watchlistAdapter: WatchlistAdapter
    private var watchlist: ArrayList<TVShow>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityWatchlistBinding = ActivityWatchlistBinding.inflate(layoutInflater)
        setContentView(activityWatchlistBinding.root)
        doInitialization()
    }

    private fun doInitialization() {
        viewModel = ViewModelProvider(this).get(WatchlistViewModel::class.java)
        activityWatchlistBinding.imageBack.setOnClickListener {
            onBackPressed()
        }
        watchlist = ArrayList()
        loadWatchlist()
    }

    private fun loadWatchlist() {
        activityWatchlistBinding.isLoading = true
        val compositeDisposableForLoadWatchlist = CompositeDisposable()
        compositeDisposableForLoadWatchlist.add(viewModel.loadWatchlist()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                activityWatchlistBinding.isLoading = false
                if (watchlist?.size!! > 0) {
                    watchlist!!.clear()
                }
                watchlist!!.addAll(it)
                watchlistAdapter = WatchlistAdapter(watchlist!!, this)
                activityWatchlistBinding.watchlistRecyclerView.adapter = watchlistAdapter
                activityWatchlistBinding.watchlistRecyclerView.visibility = View.VISIBLE
                compositeDisposableForLoadWatchlist.dispose()
            })
    }

    override fun onResume() {
        super.onResume()
        if (TempDataHolder.IS_WATCHLIST_UPDATED){
            loadWatchlist()
            TempDataHolder.IS_WATCHLIST_UPDATED = false
        }

    }

    override fun onTVShowClicked(tvShow: TVShow) {
        val intent = Intent(applicationContext, TVShowDetailsActivity::class.java)
        intent.putExtra("tvShow", tvShow)
        startActivity(intent)

    }

    override fun removeTVShowFromWatchlist(tvShow: TVShow, position: Int) {
        val compositeDisposableForDelete = CompositeDisposable()
        compositeDisposableForDelete.add(viewModel.removeTVShowFromWatchlist(tvShow)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                watchlist?.removeAt(position)
                watchlistAdapter.notifyItemRemoved(position)
                watchlistAdapter.notifyItemRangeChanged(position, watchlistAdapter.itemCount)
                compositeDisposableForDelete.dispose()
            })
    }
}