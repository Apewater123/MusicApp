package com.example.musicapp

import android.content.ContentUris
import android.content.Context
import android.media.MediaPlayer
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.MutableLiveData

class MusicController(val mContext: Context) {
    private val mMediaPlayer = MediaPlayer()
    var mPlayingSongPos = MutableLiveData<Int>(-1)
    val mListSong = ArrayList<Song>()
    private var mIsPreparing = false

    init {
        mMediaPlayer.setOnPreparedListener {
            mMediaPlayer.start()
            mIsPreparing = false
        }
    }

    fun seekTo(pos: Int) {
        mMediaPlayer.seekTo(pos)
    }

    fun getCurrentDuration(): Int {
        return mMediaPlayer.currentPosition
    }

    fun getCurrentPlayingSong(): Song? {
        if (mPlayingSongPos.value!! < 0 || mPlayingSongPos.value!! >= mListSong.size)
            return null
        return mListSong[mPlayingSongPos.value!!]
    }

    fun isPlaying(): Boolean {
        return mMediaPlayer.isPlaying || mIsPreparing
    }

    fun pause() {
        mMediaPlayer.pause()
    }

    fun start() {
        mMediaPlayer.start()
    }

    fun playSong(pos: Int) {
        mPlayingSongPos.value = pos
        val songId = mListSong[mPlayingSongPos.value!!].id
        mMediaPlayer.reset()
        val trackUri =
            ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, songId)
        try {
            mMediaPlayer.setDataSource(mContext, trackUri)
        } catch (e: Exception) {
            Log.e("MUSIC SERVICE", "Error starting data source", e)
        }
        mMediaPlayer.prepareAsync()
        mIsPreparing = true
    }

    fun playNext() {
        mPlayingSongPos.value = mPlayingSongPos.value!! + 1
        if (mPlayingSongPos.value!! >= mListSong.size) {
            mPlayingSongPos.value = 0
        }
        playSong(mPlayingSongPos.value!!)
    }

    fun playPrev() {
        mPlayingSongPos.value = mPlayingSongPos.value!! - 1
        if (mPlayingSongPos.value!! < 0) {
            mPlayingSongPos.value = mListSong.size - 1
        }
        playSong(mPlayingSongPos.value!!)
    }
}