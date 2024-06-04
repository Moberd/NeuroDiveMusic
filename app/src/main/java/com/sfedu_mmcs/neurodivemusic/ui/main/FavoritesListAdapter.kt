package com.sfedu_mmcs.neurodivemusic.ui.main

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sfedu_mmcs.neurodivemusic.databinding.FavoritesListItemBinding
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import com.sfedu_mmcs.neurodivemusic.R
import com.sfedu_mmcs.neurodivemusic.constants.getCoverUrl
import com.sfedu_mmcs.neurodivemusic.viewmodels.music.model.PlayStatus
import com.sfedu_mmcs.neurodivemusic.viewmodels.music.model.TrackData

@Throws(IOException::class)
fun drawableFromUrl(url: String?): Drawable? {
    val x: Bitmap
    val connection = URL(url).openConnection() as HttpURLConnection
    connection.connect()
    val input = connection.inputStream
    x = BitmapFactory.decodeStream(input)
    return BitmapDrawable(Resources.getSystem(), x)
}

class FavoritesListAdapter(
    val onDeleteClick: (track: TrackData) -> Unit,
    val onPlayClick: (track: TrackData) -> Unit
) :
    RecyclerView.Adapter<FavoritesListAdapter.ViewHolder>() {
    var tracksList = listOf<TrackData>()
    var currentTrack: String? = null
    var isPlaying = false

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = FavoritesListItemBinding.bind(view)

        fun bind(
            track: TrackData,
            onDeleteClick: (track: TrackData) -> Unit,
            onPlayClick: (track: TrackData) -> Unit,
            currentTrackId: String?,
            isPlaying: Boolean
        ) =
            with(binding) {
                Glide.with(itemView)
                    .load(getCoverUrl(track.id))
                    .apply(
                        RequestOptions()
                            .placeholder(R.drawable.logo.toDrawable()) // Placeholder image
                            .error(R.drawable.logo.toDrawable()) // Error image in case of loading failure
                    )
                    .into(trackCover)

                artist.text = track.artist
                trackName.text = track.name
                deleteTrack.setOnClickListener { onDeleteClick(track) }

                trackCover.setOnClickListener { onPlayClick(track) }

                if (currentTrackId != track.id) return

                trackCover.setImageResource(
                    if (isPlaying) R.drawable.pause else R.drawable.play
                )
            }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.favorites_list_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = tracksList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(tracksList[position], onDeleteClick, onPlayClick, currentTrack, isPlaying)
    }

    fun deleteTrack(elem: TrackData) {
        val new_list = tracksList.toMutableList()
        new_list.removeAt(new_list.indexOf(elem))
        setFavorites(new_list)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setFavorites(newList: List<TrackData>) {
        tracksList = newList
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setCurrentTrackId(id: String?) {
        currentTrack = id
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setPlayStatus(value: PlayStatus) {
        isPlaying = value == PlayStatus.Play
        notifyDataSetChanged()
    }
}