package org.grigiorev.rickandmortyproject.domain.entities.episode

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.grigiorev.rickandmortyproject.domain.utils.IdEntity

@Entity(tableName = "episode")
data class EpisodeEntity(
    @PrimaryKey override val id: Int,
    override val name: String,
    val air_date: String,
    val episode: String,
    val characters: List<String>,
    val url: String
) : IdEntity