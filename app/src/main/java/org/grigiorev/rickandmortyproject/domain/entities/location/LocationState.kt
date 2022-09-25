package org.grigiorev.rickandmortyproject.domain.entities.location

data class LocationState(
    var name: String = "",
    var type: String? = "",
    var dimension: String? = ""
) {
}