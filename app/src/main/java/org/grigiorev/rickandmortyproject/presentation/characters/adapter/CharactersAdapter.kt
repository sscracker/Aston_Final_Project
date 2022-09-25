package org.grigiorev.rickandmortyproject.presentation.characters.adapter

import android.view.ViewGroup
import org.grigiorev.rickandmortyproject.domain.entities.character.CharacterEntity
import org.grigiorev.rickandmortyproject.domain.utils.GenericAdapter
import org.grigiorev.rickandmortyproject.domain.utils.OnItemClick

class CharactersAdapter(listener: OnItemClick) :
    GenericAdapter<CharacterEntity, CharactersViewHolder>(listener) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharactersViewHolder {
        return CharactersViewHolder(parent, listener)
    }

    override fun onBindViewHolder(holder: CharactersViewHolder, position: Int) {
        holder.bind(entitiesList[position])
    }
}