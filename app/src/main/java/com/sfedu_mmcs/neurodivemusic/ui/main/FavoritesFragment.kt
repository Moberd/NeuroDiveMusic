package com.sfedu_mmcs.neurodivemusic.ui.main

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.sfedu_mmcs.neurodivemusic.R
import com.sfedu_mmcs.neurodivemusic.databinding.FragmentFavoritesBinding
import com.sfedu_mmcs.neurodivemusic.viewmodels.music.MusicViewModel
import com.sfedu_mmcs.neurodivemusic.viewmodels.music.model.TrackData


class FavoritesFragment : Fragment() {
    private lateinit var binding: FragmentFavoritesBinding
    private val musicViewMode: MusicViewModel by activityViewModels()

    private val favoritesListAdapter = FavoritesListAdapter({ track ->
        showDialog(track)
    }, { track ->
        if (musicViewMode.currentTrack.value?.id == track.id) {
            musicViewMode.togglePlay()
        } else {
            musicViewMode.playTrack(track.id)
        }

    })

    val favoritesObserver = Observer<List<TrackData>> {
        favoritesListAdapter.setFavorites(it)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritesBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        musicViewMode.favorites.observe(viewLifecycleOwner, favoritesObserver)

        binding.tracksHistoryList.apply {
            adapter = favoritesListAdapter
            layoutManager = LinearLayoutManager(this@FavoritesFragment.context)
        }
        val itemDecorator = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        itemDecorator.setDrawable(context?.let {
            ContextCompat.getDrawable(
                it,
                R.drawable.tracks_history_list_divider
            )
        }!!)
        binding.tracksHistoryList.addItemDecoration(itemDecorator)

        musicViewMode.status.observe(viewLifecycleOwner) { it ->
            favoritesListAdapter.setPlayStatus(it)
        }
        musicViewMode.currentTrack.observe(viewLifecycleOwner) { it ->
            favoritesListAdapter.setCurrentTrackId(it?.id)
        }
    }

    fun showDialog(trackData: TrackData) {
        val builder = AlertDialog.Builder(this.context)
        val customView = layoutInflater.inflate(R.layout.delete_track_dialog, null)
        builder.setView(customView)
        val dialog = builder.create()
        val posButton = customView.findViewById<Button>(R.id.tv2)
        posButton.setOnClickListener {
            favoritesListAdapter.deleteTrack(trackData)
            musicViewMode.removeFromFavorites(trackData.id)
            dialog.dismiss()
        }
        val negButton = customView.findViewById<Button>(R.id.tv1)
        negButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    companion object {
        @JvmStatic
        fun newInstance() = FavoritesFragment()
    }
}