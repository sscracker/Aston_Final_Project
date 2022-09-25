package org.grigiorev.rickandmortyproject.presentation.locations

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import org.grigiorev.rickandmortyproject.App
import org.grigiorev.rickandmortyproject.data.utils.Util
import org.grigiorev.rickandmortyproject.domain.entities.location.LocationEntity
import org.grigiorev.rickandmortyproject.domain.entities.location.LocationState
import org.grigiorev.rickandmortyproject.domain.utils.*
import org.grigiorev.rickandmortyproject.presentation.locations.adapter.LocationsAdapter

class LocationsViewModel(application: Application) : AndroidViewModel(application),
    LocationsGenericViewModel.ViewModel {

    private val listener = object : OnItemClick {
        override fun <T : IdEntity> onClick(entity: T) {
            openEntityDetailsLiveData.postValue(Event(entity))
        }
    }
    override val adapter: LocationsAdapter = LocationsAdapter(listener)

    override val showLoadingIndicatorLiveData: LiveData<Boolean> = MutableLiveData()
    override val setErrorModeLiveData: LiveData<Boolean> = MutableLiveData()
    override val emptyResponseLiveData: LiveData<Boolean> = MutableLiveData()
    override val renderLocationsListLiveData: LiveData<Page<LocationEntity>> =
        MutableLiveData()
    override val openEntityDetailsLiveData: LiveData<Event<IdEntity>> = MutableLiveData()

    private var lastLoadedPageNumber = 0
    private var pageToLoadNumber = 1

    private val locationState = LocationState()

    private var locationsPage: Page<LocationEntity> =
        Page(Info(0, 0, null, null), emptyList())
        set(value) {
            field = value
            renderLocationsListLiveData.postValue(value)
        }

    override fun onRecyclerViewScrolledDown() {
        if (pageToLoadNumber == lastLoadedPageNumber) {
            loadLocations(
                page = ++pageToLoadNumber,
                locationState
            )
        }
    }

    override fun onRetryButtonPressed() {
        loadLocations(page = pageToLoadNumber, locationState)
    }

    override fun onQueryTextChange(text: String) {
        lastLoadedPageNumber = 0
        pageToLoadNumber = 1
        locationState.name = text
        loadLocations(page = pageToLoadNumber, locationState)
    }


    override fun onRefresh() {
        lastLoadedPageNumber = 0
        pageToLoadNumber = 1
        loadLocations(page = pageToLoadNumber, locationState)
    }

    private var isLoading = false
        set(value) {
            field = value
            showLoadingIndicatorLiveData.postValue(value)
        }

    private var errorMode = false
        set(value) {
            field = value
            setErrorModeLiveData.postValue(value)
        }

    private var isEmptyResponse = false
        set(value) {
            field = value
            emptyResponseLiveData.postValue(value)
        }

    private var compositeDisposable = CompositeDisposable()

    init {
        loadLocations(page = pageToLoadNumber, locationState)
    }

    private fun loadLocations(
        page: Int? = null,
        locationsFilterState: LocationState
    ) {
        isLoading = true
        isEmptyResponse = false
        compositeDisposable.add(getApplication<App>().appComponent.getNetworkApi()
            .getLocationsPage(
                page,
                locationsFilterState.name,
                locationsFilterState.type,
                locationsFilterState.dimension
            )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    locationsPage = it
                    lastLoadedPageNumber = pageToLoadNumber
                    saveToLocalRepo(it)
                    isLoading = false
                    errorMode = false
                },
                onError = {
                    if (it.message?.contains(Util.EMPTY_RESPONSE_MESSAGE) != false) {
                        isLoading = false
                        errorMode = false
                        if (lastLoadedPageNumber != 0) return@subscribeBy
                        isEmptyResponse = true
                        locationsPage = Page(Info(-1, -1, null, null), emptyList())
                    } else {
                        errorMode = true
                        loadFromRoomRepo()
                    }
                }
            )
        )
    }

    private fun loadFromRoomRepo() {
        getApplication<App>().appComponent.getLocationRepo().getAll(
            Util.ENTITY_PAGE_SIZE, Util.ENTITY_PAGE_SIZE * lastLoadedPageNumber,
            locationState.name,
            locationState.type ?: "",
            locationState.dimension ?: ""
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    isLoading = false
                    val prevPage = if (lastLoadedPageNumber == 0) null else "-1"
                    locationsPage = Page(Info(-1, -1, "-1", prevPage), it)
                    if (it.isEmpty() && lastLoadedPageNumber == 0) isEmptyResponse = true
                }
            )
    }

    private fun saveToLocalRepo(page: Page<LocationEntity>) {
        compositeDisposable.add(
            getApplication<App>().appComponent.getLocationRepo().addAll(page.results).subscribe()
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}

private fun <T> LiveData<T>.postValue(value: T) {
    (this as MutableLiveData<T>).postValue(value)
}