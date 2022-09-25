package org.grigiorev.rickandmortyproject.domain.repository

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import org.grigiorev.rickandmortyproject.domain.entities.episode.EpisodeEntity

interface IEpisodesRepository {

    fun add(episode: EpisodeEntity): Single<Long>

    fun addAll(episodes: List<EpisodeEntity>): Completable

    fun getAll(
        limit: Int,
        offset: Int,
        name: String,
        episode: String,
    ): Single<List<EpisodeEntity>>

    fun getById(id: Int): Single<EpisodeEntity>

    fun getByIds(ids: List<Int>): Single<List<EpisodeEntity>>
}