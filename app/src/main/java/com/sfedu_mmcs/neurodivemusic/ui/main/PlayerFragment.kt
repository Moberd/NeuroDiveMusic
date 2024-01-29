package com.sfedu_mmcs.neurodivemusic.ui.main

import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.sfedu_mmcs.neurodivemusic.R
import com.sfedu_mmcs.neurodivemusic.viewmodels.music.model.PlayStatus
import com.sfedu_mmcs.neurodivemusic.viewmodels.music.MusicViewModel
import com.sfedu_mmcs.neurodivemusic.databinding.FragmentPlayerBinding
import com.sfedu_mmcs.neurodivemusic.viewmodels.music.model.TrackData
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener


class PlayerFragment : Fragment() {
    private lateinit var binding: FragmentPlayerBinding
    private lateinit var navController: NavController

    private val musicModel: MusicViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayerBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        with(binding) {
            lifecycle.addObserver(binding.youtubePlayerView)

            with(musicModel) {
                binding.youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        // When the YouTube player is ready, load the video specified by currentTrack
                        musicModel.currentTrack.observe(viewLifecycleOwner) {
                            if (it !is TrackData) return@observe
                            youTubePlayer.loadVideo(it.id, 0f)
                            setPlay()
                        }
                        musicModel.status.observe(viewLifecycleOwner) {
                            if (it !is PlayStatus) return@observe
                            if (it == PlayStatus.Pause) youTubePlayer.pause()
                            else youTubePlayer.play()
                        }
                        binding.youtubePlayerView.visibility = View.GONE
                    }
                    override fun onStateChange(
                        youTubePlayer: YouTubePlayer,
                        state: PlayerConstants.PlayerState
                    ) {
                        super.onStateChange(youTubePlayer, state)
                        if (state == PlayerConstants.PlayerState.ENDED) musicModel.next()
                    }
                })

                currentTrack.observe(activity as LifecycleOwner) {
                    if (it !is TrackData) return@observe

                    val spanned = SpannableString("${it.artist} \u2014 ${it.name}")
                    spanned.setSpan(
                        StyleSpan(Typeface.BOLD),
                        0,
                        it.artist.length,
                        Spannable.SPAN_INCLUSIVE_EXCLUSIVE
                    )

                    binding.trackInfo.text = spanned

                    val thumbnailUrl = "https://img.youtube.com/vi/${it.id}/0.jpg"

                    Glide.with(requireContext())
                        .load(thumbnailUrl)
                        .into(binding.trackCover)
                }

                status.observe(viewLifecycleOwner) {
                    if (it !is PlayStatus) return@observe
                    val resource = if (it == PlayStatus.Play) PlayPauseResources.Pause else PlayPauseResources.Play
                    togglePlay.setImageResource(resource)
                }

                playNext.setOnClickListener { next() }
                playPrevious.setOnClickListener { prev() }
                togglePlay.setOnClickListener { togglePlay() }
            }

        }
    }

    object PlayPauseResources {
        val Play = R.drawable.round_play_arrow_64
        val Pause = R.drawable.round_pause_64
    }

    companion object {
        @JvmStatic
        fun newInstance() = PlayerFragment()
    }
}