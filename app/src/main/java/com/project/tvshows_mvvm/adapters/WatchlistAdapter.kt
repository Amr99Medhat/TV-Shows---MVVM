package com.project.tvshows_mvvm.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.tvshows_mvvm.databinding.ItemContainerTvShowBinding
import com.project.tvshows_mvvm.listeners.WatchlistListener
import com.project.tvshows_mvvm.models.TVShow

class WatchlistAdapter(
    private val tvShows: ArrayList<TVShow>,
    private val watchlistListener: WatchlistListener
) :
    RecyclerView.Adapter<WatchlistAdapter.TVShowViewHolder>() {
    private var layoutInflater: LayoutInflater? = null

    inner class TVShowViewHolder(private val itemContainerTvShowBinding: ItemContainerTvShowBinding) :
        RecyclerView.ViewHolder(itemContainerTvShowBinding.root) {

        fun bindTVShow(tvShow: TVShow) {
            itemContainerTvShowBinding.tvShow = tvShow
            itemContainerTvShowBinding.executePendingBindings()
            itemContainerTvShowBinding.root.setOnClickListener {
                watchlistListener.onTVShowClicked(tvShow)
            }
            itemContainerTvShowBinding.imageDelete.setOnClickListener {
                watchlistListener.removeTVShowFromWatchlist(tvShow, adapterPosition)
            }
            itemContainerTvShowBinding.imageDelete.visibility = View.VISIBLE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TVShowViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val tvShowBinding = ItemContainerTvShowBinding.inflate(
            LayoutInflater.from(layoutInflater!!.context),
            parent,
            false
        )
        return TVShowViewHolder(tvShowBinding)
    }

    override fun onBindViewHolder(holder: TVShowViewHolder, position: Int) {
        holder.bindTVShow(tvShows[position])
    }

    override fun getItemCount(): Int {
        return tvShows.size
    }
}