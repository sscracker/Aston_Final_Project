package org.grigiorev.rickandmortyproject.presentation.locations.details

import androidx.lifecycle.LiveData
import org.grigiorev.rickandmortyproject.domain.entities.character.CharacterEntity
import org.grigiorev.rickandmortyproject.domain.entities.location.LocationEntity
import org.grigiorev.rickandmortyproject.domain.utils.Event
import org.grigiorev.rickandmortyproject.domain.utils.IdEntity
import org.grigiorev.rickandmortyproject.domain.utils.Page
import org.grigiorev.rickandmortyproject.presentation.characters.adapter.CharactersAdapter

interface LocationsDetailsGenericViewModel {

    interface ViewModel {

        fun onViewCreated(id: Int)
        fun onRetryButtonPressed(id: Int)

        val adapter: CharactersAdapter

        val showLoadingIndicatorLiveData: LiveData<Boolean>
        val setErrorModeLiveData: LiveData<Boolean>
        val renderLocationEntityLiveData: LiveData<LocationEntity>
        val renderCharactersListLiveData: LiveData<Page<CharacterEntity>>
        val openEntityDetailsLiveData: LiveData<Event<IdEntity>>
    }
}