package com.example.musicapp

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.musicapp.databinding.ControllerLayoutBinding
import com.example.musicapp.databinding.FragmentSongDetailBinding

class SongDetailFragment() : Fragment(R.layout.fragment_song_detail) {

    lateinit var mBinding: FragmentSongDetailBinding

    lateinit var mActivityViewModel: MainActivityViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = FragmentSongDetailBinding.bind(view)
        val controllerBinding = DataBindingUtil.setContentView<ControllerLayoutBinding>(requireActivity(),
            R.layout.controller_layout)
        mActivityViewModel = ViewModelProvider(requireActivity())[MainActivityViewModel::class.java]

        mActivityViewModel.mMusicController.mPlayingSongPos.observe(viewLifecycleOwner){songPos ->
            if(songPos >= 0) {
                val song: Song = mActivityViewModel.mMusicController.mListSong[songPos]
                mBinding.song = song
            }
        }
        controllerBinding.btnNext.setOnClickListener {
            mActivityViewModel.mMusicController.playNext()
        }
        controllerBinding.btnPrev.setOnClickListener {
            mActivityViewModel.mMusicController.playPrev()
        }
        controllerBinding.btnNext.setOnClickListener {
            mActivityViewModel.mMusicController.playPrev()
        }
        controllerBinding.btnPause.setOnClickListener {
            mActivityViewModel.mMusicController.pause()
            controllerBinding.btnPause.visibility = View.GONE
            controllerBinding.btnPlay.visibility = View.VISIBLE
        }
        controllerBinding.btnPlay.setOnClickListener {
            mActivityViewModel.mMusicController.start()
            controllerBinding.btnPlay.visibility = View.GONE
            controllerBinding.btnPause.visibility = View.VISIBLE
        }
    }
}