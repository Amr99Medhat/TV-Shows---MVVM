package com.project.tvshows_mvvm.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.tvshows_mvvm.databinding.ItemContainerTvShowBinding
import com.project.tvshows_mvvm.listeners.TVShowListener
import com.project.tvshows_mvvm.models.TVShow

class TVShowsAdapter(
    private val tvShows: ArrayList<TVShow>,
    private val tvShowListener: TVShowListener
) :
    RecyclerView.Adapter<TVShowsAdapter.TVShowViewHolder>() {
    private var layoutInflater: LayoutInflater? = null

    inner class TVShowViewHolder(private val itemContainerTvShowBinding: ItemContainerTvShowBinding) :
        RecyclerView.ViewHolder(itemContainerTvShowBinding.root) {

        fun bindTVShow(tvShow: TVShow) {
            itemContainerTvShowBinding.tvShow = tvShow
            itemContainerTvShowBinding.executePendingBindings()
            itemContainerTvShowBinding.root.setOnClickListener {
                tvShowListener.onTVShowClicked(tvShow)
            }
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