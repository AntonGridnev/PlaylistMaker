package com.practicum.playlistmaker

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class SearchAdapter(private val tracks: ArrayList<Track>) : RecyclerView.Adapter<SearchViewHolder> () {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder{
        return SearchViewHolder(parent)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int){
        holder.bind(tracks[position])
    }

    override fun getItemCount(): Int {
        return tracks.size
    }
}