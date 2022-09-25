package org.grigiorev.rickandmortyproject.domain.repository

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import org.grigiorev.rickandmortyproject.domain.entities.character.CharacterEntity

interface ICharactersRepository {

    fun add(character: CharacterEntity): Single<Long>

    fun addAll(characters: List<CharacterEntity>): Completable

    fun getAll(
        limit: Int,
        offset: Int,
        name: String,
        species: String,
        status: String,
        gender: String
    ): Single<List<CharacterEntity>>

    fun getById(id: Int): Single<CharacterEntity>

    fun getByIds(ids: List<Int>): Single<List<CharacterEntity>>
}