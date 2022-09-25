package org.grigiorev.rickandmortyproject.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import org.grigiorev.rickandmortyproject.domain.entities.location.LocationEntity

@Dao
interface LocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(location: LocationEntity): Single<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAll(locations: List<LocationEntity>): Completable

    @Query("SELECT * FROM location WHERE name LIKE '%' || :name || '%' AND type LIKE '%' || :type || '%' AND dimension LIKE '%' || :dimension || '%' LIMIT :limit OFFSET :offset")
    fun getAll(
        limit: Int,
        offset: Int,
        name: String,
        type: String,
        dimension: String
    ): Single<List<LocationEntity>>

    @Query("SELECT * FROM location WHERE id == :id")
    fun getById(id: Int): Single<LocationEntity>

    @Query("SELECT * FROM location WHERE id in (:ids)")
    fun getByIds(ids: List<Int>): Single<List<LocationEntity>>
}