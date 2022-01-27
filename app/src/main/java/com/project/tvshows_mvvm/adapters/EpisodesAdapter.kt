package com.project.tvshows_mvvm.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.tvshows_mvvm.databinding.ItemContainerEpisodesBinding
import com.project.tvshows_mvvm.models.Episode

class EpisodesAdapter(private val episode: ArrayList<Episode>) :
    RecyclerView.Adapter<EpisodesAdapter.EpisodeViewHolder>() {
    private var layoutInflater: LayoutInflater? = null

    inner class EpisodeViewHolder(private val itemContainerEpisodesBinding: ItemContainerEpisodesBinding) :
        RecyclerView.ViewHolder(itemContainerEpisodesBinding.root) {

        fun bindEpisode(episode: Episode) {
            var title = "S"
            var season = episode.season
            if (season!!.length == 1) {
                season = "0".plus(season)
            }
            var episodeNumber = episode.episode
            if (episodeNumber!!.length == 1) {
                episodeNumber = "0".plus(episodeNumber)
            }
            episodeNumber = "E".plus(episodeNumber)
            title = title.plus(season).plus(episodeNumber)
            itemContainerEpisodesBinding.title = title
            itemContainerEpisodesBinding.name = episode.name
            itemContainerEpisodesBinding.airDate = episode.airDate
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val itemContainerEpisodesBinding = ItemContainerEpisodesBinding.inflate(
            LayoutInflater.from(layoutInflater!!.context),
            parent,
            false
        )
        return EpisodeViewHolder(itemContainerEpisodesBinding)
    }

    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {
        holder.bindEpisode(episode[position])
    }

    override fun getItemCount(): Int {
        return episode.size
    }
}