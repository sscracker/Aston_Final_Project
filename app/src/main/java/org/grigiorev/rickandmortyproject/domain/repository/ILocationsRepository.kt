package org.grigiorev.rickandmortyproject.domain.repository

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import org.grigiorev.rickandmortyproject.domain.entities.location.LocationEntity

interface ILocationsRepository {

    fun add(location: LocationEntity): Single<Long>

    fun addAll(locations: List<LocationEntity>): Completable

    fun getAll(
        limit: Int,
        offset: Int,
        name: String,
        type: String,
        dimension: String,
    ): Single<List<LocationEntity>>

    fun getById(id: Int): Single<LocationEntity>

    fun getByIds(ids: List<Int>): Single<List<LocationEntity>>
}