package com.sfedu_mmcs.neurodivemusic.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.sfedu_mmcs.neurodivemusic.viewmodels.tracker.model.Emotion
import com.sfedu_mmcs.neurodivemusic.viewmodels.history.HistoryViewModel
import com.sfedu_mmcs.neurodivemusic.viewmodels.history.model.HistoryTrackData
import com.sfedu_mmcs.neurodivemusic.viewmodels.music.MusicViewModel
import com.sfedu_mmcs.neurodivemusic.viewmodels.tracker.TrackerViewModel
import com.sfedu_mmcs.neurodivemusic.viewmodels.music.model.PlayStatus
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val trackerViewModel: TrackerViewModel by viewModels()
    private val musicModel: MusicViewModel by viewModels()
    private val historyModel: HistoryViewModel by viewModels()

    lateinit var navController: NavController

    var emotions = mutableListOf<Emotion>()

    private var statusBeforePhoneLock = PlayStatus.Pause

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

        musicModel.trackChange.observe(this) {
            it?.first?.let { track ->
                if (emotions.isEmpty()) return@let

                with(track) {

                    historyModel.sendHistory(
                        HistoryTrackData(
                            id, artist, name, emotions
                        )
                    )
                }
            }

            emotions = mutableListOf()
        }

        trackerViewModel.addTrackToFavorite.observe(this) {
            if (!it || musicModel.currentTrack.value?.isFavorite == true) return@observe

            musicModel.addCurrentTrackToFavorites()
            showLikeIcon()
        }

        trackerViewModel.skipTrack.observe(this) {
            if (!it) return@observe

            musicModel.next()
        }

        setupYouTubePlayer()
    }

    private fun setupYouTubePlayer() {
        with(binding) {
            lifecycle.addObserver(youtubePlayerView)

            with(musicModel) {
                youtubePlayerView.enableBackgroundPlayback(true)
                youtubePlayerView.addYouTubePlayerListener(object :
                    AbstractYouTubePlayerListener() {

                    var lastSecond = 0

                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        trackChange.observe(this@MainActivity) {
                            if (it?.second == null || it.second.id == it.first?.id) return@observe

                            youTubePlayer.loadVideo(it.second.id, 0f)
                            lastSecond = 0
                        }
                        status.observe(this@MainActivity) {
                            if (it == PlayStatus.Pause) youTubePlayer.pause()
                            else youTubePlayer.play()
                        }
                    }


                    override fun onStateChange(
                        youTubePlayer: YouTubePlayer,
                        state: PlayerConstants.PlayerState
                    ) {
                        super.onStateChange(youTubePlayer, state)

                        if (state == PlayerConstants.PlayerState.PAUSED) setPause()
                        if (state == PlayerConstants.PlayerState.PLAYING) setPlay()
                        if (state == PlayerConstants.PlayerState.ENDED) next()
                    }

                    override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {
                        super.onCurrentSecond(youTubePlayer, second)

                        val currentSecond = second.toInt()

                        if (currentSecond <= lastSecond) return

                        lastSecond = currentSecond
                        trackerViewModel.currentEmotion.value?.let { emotions.add(it) }
                    }
                })
            }
        }
    }

    override fun onPause() {
        super.onPause()

        statusBeforePhoneLock = musicModel.status.value ?: PlayStatus.Pause
    }

    override fun onResume() {
        super.onResume()
        if (statusBeforePhoneLock == PlayStatus.Play) musicModel.setPlay()
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

    fun showLikeIcon() {
        binding.likeIcon.visibility = View.VISIBLE

        Handler(Looper.getMainLooper()).postDelayed({
            binding.likeIcon.visibility = View.GONE
        }, 1000)
    }
}