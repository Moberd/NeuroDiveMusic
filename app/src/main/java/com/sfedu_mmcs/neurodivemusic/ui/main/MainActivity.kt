package com.sfedu_mmcs.neurodivemusic.ui.main

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.sfedu_mmcs.neurodivemusic.R
import com.sfedu_mmcs.neurodivemusic.databinding.ActivityMainBinding
import com.sfedu_mmcs.neurodivemusic.viewmodels.history.HistoryViewModel
import com.sfedu_mmcs.neurodivemusic.viewmodels.history.model.HistoryTrackData
import com.sfedu_mmcs.neurodivemusic.viewmodels.music.MusicViewModel
import com.sfedu_mmcs.neurodivemusic.viewmodels.music.model.PlayStatus
import com.sfedu_mmcs.neurodivemusic.viewmodels.settings.SettingsViewModel
import com.sfedu_mmcs.neurodivemusic.viewmodels.tracker.TrackerViewModel
import com.sfedu_mmcs.neurodivemusic.viewmodels.tracker.model.Emotion
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val trackerViewModel: TrackerViewModel by viewModels()
    private val settingsModel: SettingsViewModel by viewModels()
    private val musicModel: MusicViewModel by viewModels()
    private val historyModel: HistoryViewModel by viewModels()

    lateinit var navController: NavController

    var emotions = mutableListOf<Emotion>()

    private var statusBeforePhoneLock = PlayStatus.Pause

    private fun playPreferredGenres() {
        musicModel.next(settingsModel.genresList)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("123", "create main")
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as NavHostFragment

        navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(
            navController.graph
        )

        binding.bottomNavigationView.setupWithNavController(navController)

        setContentView(binding.root)
        supportActionBar?.setDisplayShowTitleEnabled(false)

//        trackerViewModel.calibrated.observe(this) {
//            if (it) return@observe
//            val action = PlayerFragmentDirections.actionPlayerFragmentToCalibrateActivity()
//            navController.navigate(action)
//        }

        musicModel.trackChange.observe(this) {
            emotions = mutableListOf()
        }

        trackerViewModel.addTrackToFavorite.observe(this) {
            Log.i("123.addTrackToFavorite", it.toString())
        }

        trackerViewModel.addTrackToFavorite.observe(this) {
            Log.i("123.Like", it.toString())
            if (!it || musicModel.currentTrack.value?.isFavorite == true) return@observe
            Log.i("123.Like", "show like")

            musicModel.addCurrentTrackToFavorites()
            showLikeIcon()
        }

        trackerViewModel.skipTrack.observe(this) {
            if (!it) return@observe

            playPreferredGenres()
        }

        setupSettings()

        setupYouTubePlayer()

        playPreferredGenres()
    }

    private fun setupSettings() {
        // idk whether we should fill preferred genres on startup with data from gist or elsewhere
        // if so, lets just mock it for now.
        with(settingsModel) {
            settingsModel.genresList.add("rock")
            settingsModel.genresList.add("pop")
        }
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

                            musicModel.youTubePlayer.value = youTubePlayer

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
                        if (state == PlayerConstants.PlayerState.ENDED) playPreferredGenres()
                    }

                    override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {
                        super.onCurrentSecond(youTubePlayer, second)

                        val currentPlayerSecond = second.toInt()

                        if (currentPlayerSecond <= lastSecond) return

                        lastSecond = currentPlayerSecond
                        trackerViewModel.currentEmotion.value?.let { emotions.add(it) }

                        currentSecond.value = currentPlayerSecond
                    }

                    override fun onVideoDuration(
                        youTubePlayer: YouTubePlayer,
                        duration: Float
                    ) {
                        this@with.duration.value = duration.toInt()
                    }

                    override fun onVideoId(youTubePlayer: YouTubePlayer, videoId: String) {
                        currentSecond.value = 0
                        duration.value = 0
                        lastSecond = 0;
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

    fun showLikeIcon() {
        binding.likeIcon.visibility = View.VISIBLE

        Handler(Looper.getMainLooper()).postDelayed({
            binding.likeIcon.visibility = View.GONE
        }, 1000)
    }
}