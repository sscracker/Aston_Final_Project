package org.grigiorev.rickandmortyproject.presentation.episodes.details

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import org.grigiorev.rickandmortyproject.App
import org.grigiorev.rickandmortyproject.data.utils.Util
import org.grigiorev.rickandmortyproject.domain.entities.character.CharacterEntity
import org.grigiorev.rickandmortyproject.domain.entities.episode.EpisodeEntity
import org.grigiorev.rickandmortyproject.domain.utils.*
import org.grigiorev.rickandmortyproject.presentation.characters.adapter.CharactersAdapter

class EpisodeDetailsViewModel(application: Application) : AndroidViewModel(application),
    EpisodesDetailsGenericViewModel.ViewModel {

    private var episodeEntityIsLoaded = false
    private lateinit var episodeEntity: EpisodeEntity
    private var charactersFullListDownloaded = false
    private var charactersList: List<CharacterEntity> = emptyList()
        private set(value) {
            field = value
            renderCharactersListLiveData.postValue(
                Page(Info(-1, -1, "-1", null), value)
            )
        }

    private val listener = object : OnItemClick {
        override fun <T : IdEntity> onClick(entity: T) {
            openEntityDetailsLiveData.postValue(Event(entity))
        }
    }

    override val adapter: CharactersAdapter = CharactersAdapter(listener)
    override val showLoadingIndicatorLiveData: LiveData<Boolean> = MutableLiveData()
    override val openEntityDetailsLiveData: LiveData<Event<IdEntity>> = MutableLiveData()
    override val setErrorModeLiveData: LiveData<Boolean> = MutableLiveData()
    override val renderEpisodeEntityLiveData: LiveData<EpisodeEntity> = MutableLiveData()
    override val renderCharactersListLiveData: LiveData<Page<CharacterEntity>> =
        MutableLiveData()

    private var errorMode = false
        set(value) {
            field = value
            setErrorModeLiveData.postValue(value)
        }

    private var isLoading = false
        set(value) {
            field = value
            showLoadingIndicatorLiveData.postValue(value)
        }

    override fun onViewCreated(id: Int) {
        loadData(id)
    }

    override fun onRetryButtonPressed(id: Int) {
        loadData(id)
    }

    private fun loadData(id: Int) {
        if (!episodeEntityIsLoaded) {
            loadEpisodeEntity(id)
        } else if (!charactersFullListDownloaded) {
            loadCharacters()
        }
    }

    private fun loadCharacters() {
        isLoading = true
        compositeDisposable.add(
            getApplication<App>().appComponent.getNetworkApi()
                .getCharactersByIds(generateCharactersIdsString())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onSuccess = {
                        charactersList = it
                        charactersFullListDownloaded = true
                        errorMode = false
                        isLoading = false
                    },
                    onError = {
                        loadCharactersFromRepo()
                    }
                )
        )
    }

    private fun loadCharactersFromRepo() {
        compositeDisposable.add(
            getApplication<App>().appComponent.getCharactersRepo()
                .getByIds(generateCharactersIdsList())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onSuccess = {
                        charactersList = it
                        val loadedListSize = it.size
                        if (loadedListSize == episodeEntity.characters.size) {
                            charactersFullListDownloaded = true
                            errorMode = false
                        } else {
                            errorMode = true
                        }
                        isLoading = false
                    },
                    onError = {
                        errorMode = true
                        isLoading = false
                    }
                )
        )
    }

    private fun loadEpisodeEntity(id: Int) {
        isLoading = true
        compositeDisposable.add(
            getApplication<App>().appComponent.getNetworkApi()
                .getEpisodeById(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onSuccess = {
                        episodeEntity = it
                        episodeEntityIsLoaded = true
                        loadData(id)
                        errorMode = false
                        renderEpisodeEntityLiveData.postValue(it)
                        isLoading = false
                    },
                    onError = { loadEpisodeFromRepo(id) }
                )
        )
    }

    private fun loadEpisodeFromRepo(id: Int) {
        compositeDisposable.add(
            getApplication<App>().appComponent.getEpisodesRepo()
                .getById(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onSuccess = {
                        episodeEntity = it
                        episodeEntityIsLoaded = true
                        loadData(id)
                        errorMode = false
                        renderEpisodeEntityLiveData.postValue(it)
                        isLoading = false
                    },
                    onError = {
                        errorMode = true
                        isLoading = false
                    }
                )
        )
    }

    private var compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    private fun generateCharactersIdsString(): String {
        val charactersListObservable = Observable.fromIterable(episodeEntity.characters)
        val charactersIds = StringBuilder()
        charactersListObservable
            .map {
                it.split(Util.CHARACTER_URL_START)[1]
            }
            .subscribe {
                charactersIds.append(it).append(",")
            }
        return charactersIds.toString()
    }

    private fun generateCharactersIdsList(): List<Int> {
        val charactersListObservable = Observable.fromIterable(episodeEntity.characters)
        var charactersIds = ArrayList<Int>()
        charactersListObservable
            .map {
                it.split(Util.CHARACTER_URL_START)[1].toInt()
            }
            .toList()
            .subscribeBy(
                onSuccess = {
                    charactersIds = it as ArrayList<Int>
                }
            )
        return charactersIds
    }
}

private fun <T> LiveData<T>.postValue(value: T) {
    (this as MutableLiveData<T>).postValue(value)
}