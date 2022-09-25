package org.grigiorev.rickandmortyproject.data.repository

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import org.grigiorev.rickandmortyproject.data.db.CharacterDao
import org.grigiorev.rickandmortyproject.domain.entities.character.CharacterEntity
import org.grigiorev.rickandmortyproject.domain.repository.ICharactersRepository

class CharactersRepository(private val characterDao: CharacterDao) : ICharactersRepository {

    override fun add(character: CharacterEntity): Single<Long> =
        characterDao.add(character).subscribeOn(Schedulers.io())


    override fun addAll(characters: List<CharacterEntity>): Completable =
        characterDao.addAll(characters).subscribeOn(Schedulers.io())


    override fun getAll(
        limit: Int,
        offset: Int,
        name: String,
        species: String,
        status: String,
        gender: String
    ): Single<List<CharacterEntity>> =
        characterDao.getAll(limit, offset, name, species, status, gender)
            .subscribeOn(Schedulers.io())

    override fun getById(id: Int): Single<CharacterEntity> =
        characterDao.getById(id).subscribeOn(Schedulers.io())

    override fun getByIds(ids: List<Int>): Single<List<CharacterEntity>> =
        characterDao.getByIds(ids).subscribeOn(Schedulers.io())

}