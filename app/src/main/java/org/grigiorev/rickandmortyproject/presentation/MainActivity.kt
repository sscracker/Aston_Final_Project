package org.grigiorev.rickandmortyproject.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import org.grigiorev.rickandmortyproject.R
import org.grigiorev.rickandmortyproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val navController by lazy {
        val navHost =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        navHost.navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setTheme(R.style.Theme_RickAndMortyProject)
        setContentView(binding.root)

        setupNavigationUi()
    }

    private fun setupNavigationUi() {
        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController)
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.charactersFragment, R.id.episodesFragment, R.id.locationsFragment)
        )
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}