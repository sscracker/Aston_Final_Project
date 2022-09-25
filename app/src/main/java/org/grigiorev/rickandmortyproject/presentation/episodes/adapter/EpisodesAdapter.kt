package org.grigiorev.rickandmortyproject.presentation.episodes.adapter

import android.view.ViewGroup
import org.grigiorev.rickandmortyproject.domain.entities.episode.EpisodeEntity
import org.grigiorev.rickandmortyproject.domain.utils.GenericAdapter
import org.grigiorev.rickandmortyproject.domain.utils.OnItemClick

class EpisodesAdapter(listener: OnItemClick) :
    GenericAdapter<EpisodeEntity, EpisodesViewHolder>(listener) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodesViewHolder {
        return EpisodesViewHolder(parent, listener)
    }

    override fun onBindViewHolder(holder: EpisodesViewHolder, position: Int) {
        holder.bind(entitiesList[position])
    }
}