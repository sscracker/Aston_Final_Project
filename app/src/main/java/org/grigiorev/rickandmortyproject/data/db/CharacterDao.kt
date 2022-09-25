package org.grigiorev.rickandmortyproject.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import org.grigiorev.rickandmortyproject.domain.entities.character.CharacterEntity

@Dao
interface CharacterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(character: CharacterEntity): Single<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAll(characters: List<CharacterEntity>): Completable

    @Query("SELECT * FROM character WHERE name LIKE '%' || :name || '%' AND species LIKE '%' || :species || '%' AND status LIKE '%' || :status || '%' AND gender LIKE '%' || :gender || '%' LIMIT :limit OFFSET :offset")
    fun getAll(
        limit: Int,
        offset: Int,
        name: String,
        species: String,
        status: String,
        gender: String
    ): Single<List<CharacterEntity>>

    @Query("SELECT * FROM character WHERE id == :id")
    fun getById(id: Int): Single<CharacterEntity>

    @Query("SELECT * FROM character WHERE id in (:ids)")
    fun getByIds(ids: List<Int>): Single<List<CharacterEntity>>
}