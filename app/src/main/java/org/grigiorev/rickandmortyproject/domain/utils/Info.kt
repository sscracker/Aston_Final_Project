package org.grigiorev.rickandmortyproject.domain.utils

data class Info(
    val count: Int,
    val pages: Int,
    val next: String?,
    val prev: String?
)