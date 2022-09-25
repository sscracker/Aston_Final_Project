package org.grigiorev.rickandmortyproject.presentation.locations

import androidx.lifecycle.LiveData
import org.grigiorev.rickandmortyproject.domain.entities.location.LocationEntity
import org.grigiorev.rickandmortyproject.domain.utils.GenericViewModel
import org.grigiorev.rickandmortyproject.domain.utils.Page
import org.grigiorev.rickandmortyproject.presentation.locations.adapter.LocationsAdapter

interface LocationsGenericViewModel{

    interface ViewModel : GenericViewModel.ViewModel {

        /*fun onFiltersStateChange(
            filterType: LocationState.Companion.FilterType,
            filterName: String
        )*/

        val renderLocationsListLiveData: LiveData<Page<LocationEntity>>
        val adapter: LocationsAdapter
    }
}