package org.grigiorev.rickandmortyproject.presentation.characters.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.grigiorev.rickandmortyproject.databinding.ItemCharacterBinding
import org.grigiorev.rickandmortyproject.domain.entities.character.CharacterEntity
import org.grigiorev.rickandmortyproject.domain.utils.OnItemClick

class CharactersViewHolder(parent: ViewGroup, listener: OnItemClick) :
    RecyclerView.ViewHolder(
        ItemCharacterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ).root
    ) {

    init {
        itemView.setOnClickListener { listener.onClick(currentCharacter) }
    }

    private val binding = ItemCharacterBinding.bind(itemView)
    private lateinit var currentCharacter: CharacterEntity

    fun bind(characterEntity: CharacterEntity) {
        currentCharacter = characterEntity
        binding.itemCharacterNameTextView.text = characterEntity.name
        binding.itemCharacterSpeciesTextView.text = characterEntity.species
        binding.itemCharacterStatusTextView.text = characterEntity.status
        binding.itemCharacterGenderTextView.text = characterEntity.gender
        Glide
            .with(itemView.context)
            .load(characterEntity.image)
            .into(binding.characterItemImageView)
    }
}