package org.grigiorev.rickandmortyproject.presentation.episodes

import androidx.lifecycle.LiveData
import org.grigiorev.rickandmortyproject.domain.entities.episode.EpisodeEntity
import org.grigiorev.rickandmortyproject.domain.utils.GenericViewModel
import org.grigiorev.rickandmortyproject.domain.utils.Page
import org.grigiorev.rickandmortyproject.presentation.episodes.adapter.EpisodesAdapter

interface EpisodesGenericViewModel {

    interface ViewModel : GenericViewModel.ViewModel {
        val renderEpisodesListLiveData: LiveData<Page<EpisodeEntity>>
        val adapter: EpisodesAdapter
    }
}