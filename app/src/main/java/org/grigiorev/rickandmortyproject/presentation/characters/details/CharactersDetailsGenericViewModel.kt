package org.grigiorev.rickandmortyproject.presentation.characters.details

import androidx.lifecycle.LiveData
import org.grigiorev.rickandmortyproject.domain.entities.character.CharacterEntity
import org.grigiorev.rickandmortyproject.domain.entities.episode.EpisodeEntity
import org.grigiorev.rickandmortyproject.domain.entities.location.LocationEntity
import org.grigiorev.rickandmortyproject.domain.utils.Event
import org.grigiorev.rickandmortyproject.domain.utils.IdEntity
import org.grigiorev.rickandmortyproject.domain.utils.Page
import org.grigiorev.rickandmortyproject.presentation.episodes.adapter.EpisodesAdapter

interface CharactersDetailsGenericViewModel {

    interface ViewModel {

        fun onViewCreated(id: Int)
        fun onRetryButtonPressed(id: Int)
        fun onOriginLinkPressed()
        fun onLocationLinkPressed()

        val adapter: EpisodesAdapter

        val showLoadingIndicatorLiveData: LiveData<Boolean>
        val setErrorModeLiveData: LiveData<Boolean>
        val renderCharacterEntityLiveData: LiveData<CharacterEntity>
        val renderEpisodesListLiveData: LiveData<Page<EpisodeEntity>>
        val openEntityDetailsLiveData: LiveData<Event<IdEntity>>
        val openLocationDetailsLiveData: LiveData<Event<LocationEntity>>
    }
}