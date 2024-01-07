package com.sfedu_mmcs.neurodivemusic.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.sfedu_mmcs.neurodivemusic.R
import com.sfedu_mmcs.neurodivemusic.databinding.ActivityMainBinding
import com.sfedu_mmcs.neurodivemusic.viewmodels.tracker.TrackerViewModel

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val trackerViewModel: TrackerViewModel by viewModels()
    lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as NavHostFragment

        navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(
            navController.graph
        )

        val toolbarMenu = binding.toolbar.menu

        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        navController.addOnDestinationChangedListener(
            NavController.OnDestinationChangedListener { _, destination, _ ->
                val historyMenuButton = toolbarMenu.findItem(R.id.main_toolbar_historyButton)
                historyMenuButton?.setVisible(destination.id == R.id.mainNavigation_playerFragment)
            }
        )

        trackerViewModel.calibrated.observe(this) {
            if (it) return@observe
            val action = PlayerFragmentDirections.actionPlayerFragmentToCalibrateActivity()
            navController.navigate(action)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.main_toolbar_historyButton -> {
                val action = PlayerFragmentDirections.actionPlayerFragmentToTracksHistoryFragment()
                navController.navigate(action)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}