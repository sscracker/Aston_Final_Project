package org.grigiorev.rickandmortyproject.data.repository

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import org.grigiorev.rickandmortyproject.data.db.EpisodeDao
import org.grigiorev.rickandmortyproject.domain.entities.episode.EpisodeEntity
import org.grigiorev.rickandmortyproject.domain.repository.IEpisodesRepository

class EpisodesRepository(private val episodeDao: EpisodeDao) : IEpisodesRepository {

    override fun add(episode: EpisodeEntity): Single<Long> =
        episodeDao.add(episode).subscribeOn(Schedulers.io())


    override fun addAll(episodes: List<EpisodeEntity>): Completable =
        episodeDao.addAll(episodes).subscribeOn(Schedulers.io())


    override fun getAll(
        limit: Int,
        offset: Int,
        name: String,
        episode: String,
    ): Single<List<EpisodeEntity>> =
        episodeDao.getAll(limit, offset, name, episode).subscribeOn(Schedulers.io())

    override fun getById(id: Int): Single<EpisodeEntity> =
        episodeDao.getById(id).subscribeOn(Schedulers.io())

    override fun getByIds(ids: List<Int>): Single<List<EpisodeEntity>> =
        episodeDao.getByIds(ids).subscribeOn(Schedulers.io())

}