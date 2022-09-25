package org.grigiorev.rickandmortyproject.domain.entities.location

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.grigiorev.rickandmortyproject.domain.utils.IdEntity

@Entity(tableName = "location")
data class LocationEntity(
    @PrimaryKey override val id: Int,
    override val name: String,
    val type: String,
    val dimension: String,
    val residents: List<String>,
    val url: String
) : IdEntity