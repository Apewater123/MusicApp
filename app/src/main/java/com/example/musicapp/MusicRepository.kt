package com.example.musicapp

import android.content.Context
import android.os.Build
import android.provider.MediaStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MusicRepository(val context: Context) {
    suspend fun getAllMusic(): List<Song> {
        return withContext(Dispatchers.IO){
            val listAllSong = ArrayList<Song>()

            val contentResolver = context.contentResolver
            val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            val projection = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q)
                arrayOf(
                    MediaStore.Audio.Media._ID,
                    MediaStore.Audio.Media.TITLE,
                    MediaStore.Audio.Media.DURATION,
                    MediaStore.Audio.Media.ARTIST,
                    MediaStore.Audio.Media.AUTHOR,
                    MediaStore.Audio.Media.ALBUM_ID,
                    MediaStore.Audio.Media.ALBUM
                ) else
                arrayOf(
                    MediaStore.Audio.Media._ID,
                    MediaStore.Audio.Media.TITLE,
                    MediaStore.Audio.Media.DURATION,
                    MediaStore.Audio.Media.ARTIST,
                    MediaStore.Audio.Media.ALBUM_ID,
                    MediaStore.Audio.Media.ALBUM)

            val select = MediaStore.Audio.Media.IS_MUSIC + "=?"
            val selectArgs = arrayOf("1")
            val orderBy = MediaStore.Audio.Media.TITLE + " ASC"
            val cursor = contentResolver.query(uri, projection, select, selectArgs, orderBy)

            if(cursor != null){
                val idIndex = cursor.getColumnIndex(MediaStore.Audio.Media._ID)
                val titleIndex = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
                val durationIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)
                val artistIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)
                val authorIndex = cursor.getColumnIndex(MediaStore.Audio.Media.AUTHOR)
                val albumIdIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)
                val albumTitleIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)
                while(cursor.moveToNext()){
                    val id = cursor.getLong(idIndex)
                    val title = cursor.getString(titleIndex)
                    val duration = cursor.getLong(durationIndex)
                    val artist = cursor.getString(artistIndex)
                    val author = cursor.getString(authorIndex)
                    val albumId = cursor.getLong(albumIdIndex)
                    val albumTitle = cursor.getString(albumTitleIndex)
                    val song = Song(id, title, duration, artist, author, albumId, albumTitle)
                    listAllSong.add(song)
                }

                cursor.close()
            }

            return@withContext listAllSong
        }
    }
}