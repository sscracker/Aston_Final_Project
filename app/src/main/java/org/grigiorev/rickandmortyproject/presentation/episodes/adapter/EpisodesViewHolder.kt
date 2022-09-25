package org.grigiorev.rickandmortyproject.presentation.episodes.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.grigiorev.rickandmortyproject.databinding.ItemEpisodeBinding
import org.grigiorev.rickandmortyproject.domain.entities.episode.EpisodeEntity
import org.grigiorev.rickandmortyproject.domain.utils.OnItemClick

class EpisodesViewHolder(parent: ViewGroup, listener: OnItemClick) :
    RecyclerView.ViewHolder(
        ItemEpisodeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ).root
    ) {

    init {
        itemView.setOnClickListener { listener.onClick(currentEpisode) }
    }

    private val binding = ItemEpisodeBinding.bind(itemView)
    private lateinit var currentEpisode: EpisodeEntity

    fun bind(episodeEntity: EpisodeEntity) {
        currentEpisode = episodeEntity
        binding.itemEpisodeNameTextView.text = episodeEntity.name
        binding.itemEpisodeNumberTextView.text = episodeEntity.episode
        binding.itemEpisodeAirDateTextView.text = episodeEntity.air_date
    }
}