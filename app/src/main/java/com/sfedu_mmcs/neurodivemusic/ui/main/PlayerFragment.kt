package com.sfedu_mmcs.neurodivemusic.ui.main

import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
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

                duration.observe(viewLifecycleOwner) {
                    seekBar.max = it

                    val minutest = (it / 60).toInt()
                    val seconds = it % 60

                    binding.duretion.setText("${minutest.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}")
                }

                currentSecond.observe(viewLifecycleOwner) {
                    seekBar.progress = it


                    val minutest = (it / 60).toInt()
                    val seconds = it % 60

                    binding.currentTime.setText("${minutest.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}")
                }

                seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(
                        seekBar: SeekBar?,
                        progress: Int,
                        fromUser: Boolean
                    ) {
                        youTubePlayer.value?.seekTo(progress.toFloat())
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    }

                    override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    }

                })


                playNext.setOnClickListener { next() }
                playPrevious.setOnClickListener { prev() }
                togglePlay.setOnClickListener { togglePlay() }
            }

        }
    }

    object PlayPauseResources {
        val Play = R.drawable.play
        val Pause = R.drawable.pause
    }

    companion object {
        @JvmStatic
        fun newInstance() = PlayerFragment()
    }
}