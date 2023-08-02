package com.example.musicapp

data class Song(
    val id: Long,
    val title: String?,
    val duration: Long,
    val artist: String?,
    val author: String?,
    val albumId: Long?,
    val albumTitle: String?
)
