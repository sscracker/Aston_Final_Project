package org.grigiorev.rickandmortyproject.presentation.characters

import androidx.lifecycle.LiveData
import org.grigiorev.rickandmortyproject.domain.entities.character.CharacterEntity
import org.grigiorev.rickandmortyproject.domain.entities.character.CharacterState
import org.grigiorev.rickandmortyproject.domain.utils.GenericViewModel
import org.grigiorev.rickandmortyproject.domain.utils.Page
import org.grigiorev.rickandmortyproject.presentation.characters.adapter.CharactersAdapter

interface CharacterGenericViewModel {

    interface ViewModel : GenericViewModel.ViewModel {


        val renderCharactersListLiveData: LiveData<Page<CharacterEntity>>
        val adapter: CharactersAdapter
    }
}