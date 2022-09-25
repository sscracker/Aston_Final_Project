package org.grigiorev.rickandmortyproject.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import org.grigiorev.rickandmortyproject.domain.entities.episode.EpisodeEntity

@Dao
interface EpisodeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(episode: EpisodeEntity): Single<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAll(episodes: List<EpisodeEntity>): Completable

    @Query("SELECT * FROM episode WHERE name LIKE '%' || :name || '%' AND episode LIKE '%' || :episode || '%' LIMIT :limit OFFSET :offset")
    fun getAll(
        limit: Int,
        offset: Int,
        name: String,
        episode: String
    ): Single<List<EpisodeEntity>>

    @Query("SELECT * FROM episode WHERE id == :id")
    fun getById(id: Int): Single<EpisodeEntity>

    @Query("SELECT * FROM episode WHERE id in (:ids)")
    fun getByIds(ids: List<Int>): Single<List<EpisodeEntity>>
}