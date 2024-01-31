package com.sfedu_mmcs.neurodivemusic.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.sfedu_mmcs.neurodivemusic.R
import com.sfedu_mmcs.neurodivemusic.databinding.ActivityMainBinding
import com.sfedu_mmcs.neurodivemusic.viewmodels.music.MusicViewModel
import com.sfedu_mmcs.neurodivemusic.viewmodels.music.model.TrackData
import com.sfedu_mmcs.neurodivemusic.viewmodels.tracker.TrackerViewModel
import com.sfedu_mmcs.neurodivemusic.viewmodels.music.model.PlayStatus

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val trackerViewModel: TrackerViewModel by viewModels()
    private val musicModel: MusicViewModel by viewModels()

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

        setupYouTubePlayer()
    }

    private fun setupYouTubePlayer() {
        with(binding) {
            lifecycle.addObserver(youtubePlayerView)
            with(musicModel) {
                youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        // When the YouTube player is ready, load the video specified by currentTrack
                        currentTrack.observe(this@MainActivity) {
                            if (it !is TrackData) return@observe
                            youTubePlayer.loadVideo(it.id, 0f)
                            musicModel.setPlay()
                        }
                        status.observe(this@MainActivity) {
                            if (it !is PlayStatus) return@observe
                            if (it == PlayStatus.Pause) youTubePlayer.pause()
                            else youTubePlayer.play()
                        }
                        youtubePlayerView.visibility = View.GONE
                    }

                    override fun onStateChange(
                        youTubePlayer: YouTubePlayer,
                        state: PlayerConstants.PlayerState
                    ) {
                        super.onStateChange(youTubePlayer, state)
                        if (state == PlayerConstants.PlayerState.ENDED) musicModel.next()
                    }
                })
            }
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