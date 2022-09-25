package org.grigiorev.rickandmortyproject.domain.entities.character

data class CharacterState(
    var name: String = "",
    var species: String? = "",
    var status: String? = "",
    var gender: String? = "",
) {

}
