package org.grigiorev.rickandmortyproject.data.repository

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import org.grigiorev.rickandmortyproject.data.db.LocationDao
import org.grigiorev.rickandmortyproject.domain.entities.location.LocationEntity
import org.grigiorev.rickandmortyproject.domain.repository.ILocationsRepository

class LocationsRepository(private val locationDao: LocationDao) : ILocationsRepository {

    override fun add(location: LocationEntity): Single<Long> =
        locationDao.add(location).subscribeOn(Schedulers.io())


    override fun addAll(locations: List<LocationEntity>): Completable =
        locationDao.addAll(locations).subscribeOn(Schedulers.io())


    override fun getAll(
        limit: Int,
        offset: Int,
        name: String,
        type: String,
        dimension: String
    ): Single<List<LocationEntity>> =
        locationDao.getAll(limit, offset, name, type, dimension).subscribeOn(Schedulers.io())

    override fun getById(id: Int): Single<LocationEntity> =
        locationDao.getById(id).subscribeOn(Schedulers.io())

    override fun getByIds(ids: List<Int>): Single<List<LocationEntity>> =
        locationDao.getByIds(ids).subscribeOn(Schedulers.io())
}