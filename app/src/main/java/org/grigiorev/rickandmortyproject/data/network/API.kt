package org.grigiorev.rickandmortyproject.data.network

import io.reactivex.rxjava3.core.Single
import org.grigiorev.rickandmortyproject.domain.entities.character.CharacterEntity
import org.grigiorev.rickandmortyproject.domain.entities.episode.EpisodeEntity
import org.grigiorev.rickandmortyproject.domain.entities.location.LocationEntity
import org.grigiorev.rickandmortyproject.domain.utils.Page
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface API {

    @GET("character")
    fun getCharacterPage(
        @Query("page") page: Int? = null,
        @Query("name") name: String? = null,
        @Query("status") status: String? = null,
        @Query("species") species: String? = null,
        @Query("gender") gender: String? = null
    ): Single<Page<CharacterEntity>>

    @GET("episode")
    fun getEpisodesPage(
        @Query("page") page: Int? = null,
        @Query("name") name: String? = null,
        @Query("episode") episode: String? = null,
    ): Single<Page<EpisodeEntity>>

    @GET("location")
    fun getLocationsPage(
        @Query("page") page: Int? = null,
        @Query("name") name: String? = null,
        @Query("type") type: String? = null,
        @Query("dimension") dimension: String? = null,
    ): Single<Page<LocationEntity>>

    @GET("character/{id}")
    fun getCharacterById(@Path("id") id: Int): Single<CharacterEntity>

    @GET("character/{ids}")
    fun getCharactersByIds(@Path("ids") ids: String): Single<List<CharacterEntity>>

    @GET("episode/{id}")
    fun getEpisodeById(@Path("id") id: Int): Single<EpisodeEntity>

    @GET("episode/{ids}")
    fun getEpisodesByIds(@Path("ids") ids: String): Single<List<EpisodeEntity>>

    @GET("location/{id}")
    fun getLocationById(@Path("id") id: Int): Single<LocationEntity>

    @GET("location/{ids}")
    fun getLocationsByIds(@Path("ids") ids: String): Single<List<LocationEntity>>
}