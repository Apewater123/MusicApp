package com.example.musicapp

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainActivityViewModel(context: Application) : AndroidViewModel(context) {
    private val musicRepository: MusicRepository
    var mMusicController: MusicController
    init {
        musicRepository = MusicRepository(context)
        mMusicController = MusicController(context.applicationContext)
    }

    fun getAllMusic(): LiveData<List<Song>> {
        val liveDataListSong = MutableLiveData<List<Song>>(ArrayList<Song>())

        viewModelScope.launch {
            liveDataListSong.value = musicRepository.getAllMusic()
        }

        return liveDataListSong
    }
}