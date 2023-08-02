package com.example.musicapp

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.musicapp.databinding.SongItemBinding

class SongListAdapter(val mContext: Context, val mListSong: List<Song>, val onSongClickListener: OnSongClickListener) : Adapter<SongListAdapter.SongViewHolder>() {

    var mPlayingPos = -1
    var mIsPlaying = false

    interface OnSongClickListener {
        fun onSongClick(pos: Int)
    }

    fun notifySongChange(playingPos: Int, isPlaying: Boolean) {
        val oldPos = mPlayingPos
        mIsPlaying = isPlaying
        mPlayingPos = playingPos
        notifyItemChanged(oldPos)
        notifyItemChanged(mPlayingPos)
    }

    inner class SongViewHolder(itemView: View) : ViewHolder(itemView){
        val mBinding = SongItemBinding.bind(itemView)

        init {
            itemView.setOnClickListener {
                onSongClickListener.onSongClick(adapterPosition)
            }
        }

        fun bind(song: Song){

            mBinding.song = song

            if(adapterPosition == mPlayingPos){
                if(mIsPlaying){
                    itemView.setBackgroundColor(Color.GREEN)
                } else {
                    itemView.setBackgroundColor(Color.YELLOW)
                }
            } else {
                itemView.setBackgroundColor(Color.TRANSPARENT)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        return SongViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.song_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return mListSong.size
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        holder.bind(mListSong[position])
    }
}