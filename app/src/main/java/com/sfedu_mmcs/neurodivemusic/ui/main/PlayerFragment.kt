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
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sfedu_mmcs.neurodivemusic.R
import com.sfedu_mmcs.neurodivemusic.constants.getCoverUrl
import com.sfedu_mmcs.neurodivemusic.viewmodels.music.model.PlayStatus
import com.sfedu_mmcs.neurodivemusic.viewmodels.music.MusicViewModel
import com.sfedu_mmcs.neurodivemusic.databinding.FragmentPlayerBinding
import com.sfedu_mmcs.neurodivemusic.viewmodels.music.model.TrackData
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
            with(musicModel) {
                currentTrack.observe(viewLifecycleOwner) {
                    if (it !is TrackData) return@observe

                    val spannedArtist = SpannableString(it.artist)
                    spannedArtist.setSpan(
                        StyleSpan(Typeface.BOLD),
                        0,
                        it.artist.length,
                        Spannable.SPAN_INCLUSIVE_EXCLUSIVE
                    )

                    artistName.text = spannedArtist

                    val spannedTrack = SpannableString(it.name)
                    spannedTrack.setSpan(
                        StyleSpan(Typeface.BOLD),
                        0,
                        it.name.length,
                        Spannable.SPAN_INCLUSIVE_EXCLUSIVE
                    )

                    trackName.text = spannedTrack

                    favoriteStatus.setImageResource(
                        if (it.isFavorite) R.drawable.round_thumb_up_60
                        else R.drawable.outline_thumb_up_24
                    )

                    val thumbnailUrl = getCoverUrl(it.id)

                    Glide.with(requireContext())
                        .load(thumbnailUrl)
                        .apply(
                            RequestOptions()
                                .placeholder(R.drawable.logo.toDrawable())
                                .error(R.drawable.logo.toDrawable())
                        )
                        .into(trackCover)
                }

                status.observe(viewLifecycleOwner) {
                    if (it !is PlayStatus) return@observe
                    val resource =
                        if (it == PlayStatus.Play) PlayPauseResources.Pause else PlayPauseResources.Play
                    togglePlay.setImageResource(resource)
                }

                playNext.setOnClickListener { next() }
                playPrevious.setOnClickListener { prev() }
                togglePlay.setOnClickListener { togglePlay() }
            }

        }
    }

    object PlayPauseResources {
        val Play = R.drawable.baseline_play_circle_24
        val Pause = R.drawable.baseline_pause_circle_24
    }

    companion object {
        @JvmStatic
        fun newInstance() = PlayerFragment()
    }
}