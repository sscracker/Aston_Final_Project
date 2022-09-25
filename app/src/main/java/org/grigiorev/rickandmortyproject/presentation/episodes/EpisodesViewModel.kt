package org.grigiorev.rickandmortyproject.presentation.episodes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import org.grigiorev.rickandmortyproject.App
import org.grigiorev.rickandmortyproject.data.utils.Util
import org.grigiorev.rickandmortyproject.domain.entities.episode.EpisodeEntity
import org.grigiorev.rickandmortyproject.domain.entities.episode.EpisodeState
import org.grigiorev.rickandmortyproject.domain.utils.*
import org.grigiorev.rickandmortyproject.presentation.episodes.adapter.EpisodesAdapter

class EpisodesViewModel(application: Application) : AndroidViewModel(application),
    EpisodesGenericViewModel.ViewModel {
    private var compositeDisposable = CompositeDisposable()
    private val listener = object : OnItemClick {
        override fun <T : IdEntity> onClick(entity: T) {
            openEntityDetailsLiveData.postValue(Event(entity))
        }
    }
    override val adapter: EpisodesAdapter = EpisodesAdapter(listener)

    override val showLoadingIndicatorLiveData: LiveData<Boolean> = MutableLiveData()
    override val setErrorModeLiveData: LiveData<Boolean> = MutableLiveData()
    override val emptyResponseLiveData: LiveData<Boolean> = MutableLiveData()
    override val renderEpisodesListLiveData: LiveData<Page<EpisodeEntity>> =
        MutableLiveData()
    override val openEntityDetailsLiveData: LiveData<Event<IdEntity>> = MutableLiveData()

    private var lastLoadedPageNumber = 0
    private var pageToLoadNumber = 1

    private val episodesState = EpisodeState()

    private var episodesPage: Page<EpisodeEntity> =
        Page(Info(0, 0, null, null), emptyList())
        set(value) {
            field = value
            renderEpisodesListLiveData.postValue(value)
        }

    override fun onRecyclerViewScrolledDown() {
        if (pageToLoadNumber == lastLoadedPageNumber) {
            loadEpisodes(
                page = ++pageToLoadNumber,
                episodesState
            )
        }
    }

    override fun onRetryButtonPressed() {
        loadEpisodes(page = pageToLoadNumber, episodesState)
    }

    override fun onQueryTextChange(text: String) {
        lastLoadedPageNumber = 0
        pageToLoadNumber = 1
        episodesState.name = text
        loadEpisodes(page = pageToLoadNumber, episodesState)
    }


    override fun onRefresh() {
        lastLoadedPageNumber = 0
        pageToLoadNumber = 1
        loadEpisodes(page = pageToLoadNumber, episodesState)
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



    init {
        loadEpisodes(page = pageToLoadNumber, episodesState)
    }

    private fun loadEpisodes(
        page: Int? = null,
        episodesFilterState: EpisodeState
    ) {
        isLoading = true
        isEmptyResponse = false
        compositeDisposable.add(getApplication<App>().appComponent.getNetworkApi()
            .getEpisodesPage(
                page,
                episodesFilterState.name,
                episodesFilterState.episodeName
            )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    episodesPage = it
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
                        episodesPage = Page(Info(-1, -1, null, null), emptyList())
                    } else {
                        errorMode = true
                        loadFromRoomRepo()
                    }
                }
            )
        )
    }

    private fun loadFromRoomRepo() {
        getApplication<App>().appComponent.getEpisodesRepo().getAll(
            Util.ENTITY_PAGE_SIZE, Util.ENTITY_PAGE_SIZE * lastLoadedPageNumber,
            episodesState.name,
            episodesState.episodeName
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    isLoading = false
                    val prevPage = if (lastLoadedPageNumber == 0) null else "-1"
                    episodesPage = Page(Info(-1, -1, "-1", prevPage), it)
                    if (it.isEmpty() && lastLoadedPageNumber == 0) isEmptyResponse = true
                }
            )
    }

    private fun saveToLocalRepo(page: Page<EpisodeEntity>) {
        compositeDisposable.add(
            getApplication<App>().appComponent.getEpisodesRepo().addAll(page.results).subscribe()
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