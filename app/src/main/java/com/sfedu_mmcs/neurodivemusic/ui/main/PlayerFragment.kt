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
import com.sfedu_mmcs.neurodivemusic.R
import com.sfedu_mmcs.neurodivemusic.viewmodels.music.model.PlayStatus
import com.sfedu_mmcs.neurodivemusic.viewmodels.music.MusicViewModel
import com.sfedu_mmcs.neurodivemusic.databinding.FragmentPlayerBinding
import com.sfedu_mmcs.neurodivemusic.viewmodels.music.model.TrackData
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
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

        val youTubePlayerView: YouTubePlayerView = view.findViewById(R.id.youtubePlayerView)
        lifecycle.addObserver(youTubePlayerView)

        navController = findNavController()

        with(binding) {
            with(musicModel) {

                youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        // When the YouTube player is ready, load the video specified by currentTrack
                        musicModel.currentTrack.observe(viewLifecycleOwner) { trackData ->
                            if (trackData !is TrackData) return@observe
                            youTubePlayer.loadVideo(trackData.videoId, 0f)
                            youTubePlayer.play()
                        }
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
                    // TODO retrieve when yt audio player's ready
                    // trackCover.setImageDrawable(it.cover)
                }

                status.observe(activity as LifecycleOwner) {
                    val resource =
                        if (it == PlayStatus.Play) PlayPauseResources.Play else PlayPauseResources.Pause
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