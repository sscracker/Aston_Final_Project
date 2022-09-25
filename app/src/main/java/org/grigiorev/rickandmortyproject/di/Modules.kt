package org.grigiorev.rickandmortyproject.di

import android.content.Context
import androidx.room.Room
import dagger.Component
import dagger.Module
import dagger.Provides
import org.grigiorev.rickandmortyproject.data.db.*
import org.grigiorev.rickandmortyproject.data.network.API
import org.grigiorev.rickandmortyproject.data.repository.CharactersRepository
import org.grigiorev.rickandmortyproject.data.repository.EpisodesRepository
import org.grigiorev.rickandmortyproject.data.repository.LocationsRepository
import org.grigiorev.rickandmortyproject.domain.repository.ICharactersRepository
import org.grigiorev.rickandmortyproject.domain.repository.IEpisodesRepository
import org.grigiorev.rickandmortyproject.domain.repository.ILocationsRepository
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class DatabaseModule(private val context: Context) {

    @Provides
    @Singleton
    fun provideContext(): Context = context

    @Provides
    @Singleton
    fun provideCharactersRepo(characterDao: CharacterDao): ICharactersRepository =
        CharactersRepository(characterDao)

    @Provides
    @Singleton
    fun provideCharacterDao(dataBase: AppDatabase): CharacterDao = dataBase.characterDao()

    @Provides
    @Singleton
    fun provideDb(context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "app.db").build()

    @Provides
    @Singleton
    fun provideEpisodesRepo(episodeDao: EpisodeDao): IEpisodesRepository =
        EpisodesRepository(episodeDao)

    @Provides
    @Singleton
    fun provideEpisodeDao(dataBase: AppDatabase): EpisodeDao = dataBase.episodesDao()

    @Provides
    @Singleton
    fun provideLocationsRepo(locationDao: LocationDao): ILocationsRepository =
        LocationsRepository(locationDao)

    @Provides
    @Singleton
    fun provideLocationDao(dataBase: AppDatabase): LocationDao = dataBase.locationsDao()
}

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofitApi(): API {
        val retrofit = Retrofit
            .Builder()
            .baseUrl("https://rickandmortyapi.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
        return retrofit.create(API::class.java)
    }
}

@Singleton
@Component(modules = [DatabaseModule::class, NetworkModule::class])
interface AppComponent {
    fun getCharactersRepo(): ICharactersRepository
    fun getEpisodesRepo(): IEpisodesRepository
    fun getLocationRepo(): ILocationsRepository
    fun getNetworkApi(): API
}
