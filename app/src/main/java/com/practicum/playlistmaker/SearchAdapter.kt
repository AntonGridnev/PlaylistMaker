package com.practicum.playlistmaker

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class SearchAdapter(private val tracks: List<Track>,
                    private val onItemClickListener: OnItemClickListener? = null
) : RecyclerView.Adapter<SearchViewHolder> () {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder{
        return SearchViewHolder(parent)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int){
        holder.bind(tracks[position])
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener {
                onItemClickListener.onItemClick(tracks[holder.adapterPosition])
            }
        }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }
}