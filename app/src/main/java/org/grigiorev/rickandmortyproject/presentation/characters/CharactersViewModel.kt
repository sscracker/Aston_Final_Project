package org.grigiorev.rickandmortyproject.presentation.characters

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import org.grigiorev.rickandmortyproject.App
import org.grigiorev.rickandmortyproject.data.utils.Util
import org.grigiorev.rickandmortyproject.domain.entities.character.CharacterEntity
import org.grigiorev.rickandmortyproject.domain.entities.character.CharacterState
import org.grigiorev.rickandmortyproject.domain.utils.*
import org.grigiorev.rickandmortyproject.presentation.characters.adapter.CharactersAdapter

class CharactersViewModel(application: Application) : AndroidViewModel(application),
    CharacterGenericViewModel.ViewModel {

    private val listener = object : OnItemClick {
        override fun <T : IdEntity> onClick(entity: T) {
            openEntityDetailsLiveData.postValue(Event(entity))
        }
    }
    override val adapter: CharactersAdapter = CharactersAdapter(listener)

    override val showLoadingIndicatorLiveData: LiveData<Boolean> = MutableLiveData()
    override val setErrorModeLiveData: LiveData<Boolean> = MutableLiveData()
    override val emptyResponseLiveData: LiveData<Boolean> = MutableLiveData()
    override val renderCharactersListLiveData: LiveData<Page<CharacterEntity>> =
        MutableLiveData()
    override val openEntityDetailsLiveData: LiveData<Event<IdEntity>> = MutableLiveData()

    private var lastLoadedPageNumber = 0
    private var pageToLoadNumber = 1

    private val charactersState = CharacterState()

    private var charactersPage: Page<CharacterEntity> =
        Page(Info(0, 0, null, null), emptyList())
        set(value) {
            field = value
            renderCharactersListLiveData.postValue(value)
        }

    override fun onRecyclerViewScrolledDown() {
        if (pageToLoadNumber == lastLoadedPageNumber) {
            loadCharacters(
                page = ++pageToLoadNumber,
                charactersState
            )
        }
    }

    override fun onRetryButtonPressed() {
        loadCharacters(page = pageToLoadNumber, charactersState)
    }

    override fun onQueryTextChange(text: String) {
        lastLoadedPageNumber = 0
        pageToLoadNumber = 1
        charactersState.name = text
        loadCharacters(page = pageToLoadNumber, charactersState)
    }



    override fun onRefresh() {
        lastLoadedPageNumber = 0
        pageToLoadNumber = 1
        loadCharacters(page = pageToLoadNumber, charactersState)
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
        loadCharacters(page = pageToLoadNumber, charactersState)
    }

    private fun loadCharacters(
        page: Int? = null,
        charactersFilterState: CharacterState
    ) {
        isLoading = true
        isEmptyResponse = false
        compositeDisposable.add(getApplication<App>().appComponent.getNetworkApi()
            .getCharacterPage(
                page,
                charactersFilterState.name,
                charactersFilterState.status,
                charactersFilterState.species,
                charactersFilterState.gender
            )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    charactersPage = it
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
                        charactersPage = Page(Info(-1, -1, null, null), emptyList())
                    } else {
                        errorMode = true
                        loadFromRoomRepo()
                    }
                }
            )
        )
    }

    private fun loadFromRoomRepo() {
        getApplication<App>().appComponent.getCharactersRepo().getAll(
            Util.ENTITY_PAGE_SIZE, Util.ENTITY_PAGE_SIZE * lastLoadedPageNumber,
            charactersState.name,
            charactersState.species ?: "",
            charactersState.status ?: "",
            charactersState.gender ?: ""
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    isLoading = false
                    val prevPage = if (lastLoadedPageNumber == 0) null else "-1"
                    charactersPage = Page(Info(-1, -1, "-1", prevPage), it)
                    if (it.isEmpty() && lastLoadedPageNumber == 0) isEmptyResponse = true
                }
            )
    }

    private fun saveToLocalRepo(page: Page<CharacterEntity>) {
        compositeDisposable.add(
            getApplication<App>().appComponent.getCharactersRepo().addAll(page.results).subscribe()
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