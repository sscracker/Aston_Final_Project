package org.grigiorev.rickandmortyproject.domain.utils

data class Page<T>(
    val info: Info,
    val results: List<T>
)