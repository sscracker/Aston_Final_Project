package org.grigiorev.rickandmortyproject.domain.utils

import androidx.lifecycle.LiveData

interface GenericViewModel {

    interface ViewModel {

        fun onRecyclerViewScrolledDown()
        fun onRetryButtonPressed()
        fun onQueryTextChange(text: String)
        fun onRefresh()

        val showLoadingIndicatorLiveData: LiveData<Boolean>
        val setErrorModeLiveData: LiveData<Boolean>
        val emptyResponseLiveData: LiveData<Boolean>
        val openEntityDetailsLiveData: LiveData<Event<IdEntity>>
    }
}