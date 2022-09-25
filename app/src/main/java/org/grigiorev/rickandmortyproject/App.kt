package org.grigiorev.rickandmortyproject

import android.app.Application
import android.content.Context
import androidx.fragment.app.Fragment
import org.grigiorev.rickandmortyproject.di.AppComponent
import org.grigiorev.rickandmortyproject.di.DaggerAppComponent
import org.grigiorev.rickandmortyproject.di.DatabaseModule

class App : Application() {
    val appComponent: AppComponent by lazy {
        DaggerAppComponent
            .builder()
            .databaseModule(DatabaseModule(this))
            .build()
    }
}

val Context.app: App
    get() = applicationContext as App

val Fragment.app: App
    get() = requireActivity().app