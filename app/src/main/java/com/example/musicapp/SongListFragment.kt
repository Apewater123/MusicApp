package com.example.musicapp

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicapp.databinding.FragmentSongListBinding

class SongListFragment : Fragment(R.layout.fragment_song_list) {
    lateinit var mSongAdapter: SongListAdapter

    lateinit var mBinding: FragmentSongListBinding

    lateinit var mActivityViewModel: MainActivityViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = FragmentSongListBinding.bind(view)

        mActivityViewModel = ViewModelProvider(requireActivity())[MainActivityViewModel::class.java]

        mActivityViewModel.mMusicController = MusicController(requireContext())

        mSongAdapter = SongListAdapter(
            requireContext(),
            mActivityViewModel.mMusicController.mListSong,
            object :
                SongListAdapter.OnSongClickListener {
                override fun onSongClick(pos: Int) {
                    if (pos == mActivityViewModel.mMusicController.mPlayingSongPos.value) {
                        if (mActivityViewModel.mMusicController.isPlaying()) {
                            mActivityViewModel.mMusicController.pause()
                        } else {
                            mActivityViewModel.mMusicController.start()
                        }
                    } else {
                        mActivityViewModel.mMusicController.playSong(pos)
                    }
                    mSongAdapter.notifySongChange(
                        pos,
                        mActivityViewModel.mMusicController.isPlaying()
                    )

                    val currentOrientation = resources.configuration.orientation
                    if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                        // Landscape
                    } else {
                        // Portrait
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.host_fragment, SongDetailFragment::class.java, null)
                            .addToBackStack("song_detail")
                            .commit()
                    }
                }
            })

        mBinding.rcvSong.layoutManager = LinearLayoutManager(requireContext())
        mBinding.rcvSong.adapter = mSongAdapter
        requirePermission()
    }

    interface PermissionRequestDone {
        fun onAccept()
    }

    private val permissionRequestDone = object : PermissionRequestDone {
        override fun onAccept() {
            mActivityViewModel.getAllMusic().observe(viewLifecycleOwner) { listSong ->
                mActivityViewModel.mMusicController.mListSong.addAll(listSong)
                mSongAdapter.notifyDataSetChanged()
                Log.d("PhucDVb", "onAccept: " + listSong.size)
            }
        }
    }

    private fun requirePermission(): Boolean {

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            == PackageManager.PERMISSION_DENIED
        ) {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 999)
            return false
        }
        permissionRequestDone.onAccept()
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

    }

}