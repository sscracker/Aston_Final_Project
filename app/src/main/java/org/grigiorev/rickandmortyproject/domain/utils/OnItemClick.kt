package org.grigiorev.rickandmortyproject.domain.utils

interface OnItemClick {

    fun <T : IdEntity> onClick(entity: T)
}