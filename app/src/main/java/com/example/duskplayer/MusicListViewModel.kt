package com.example.duskplayer

import androidx.lifecycle.ViewModel

class MusicListViewModel() : ViewModel() {

    private var musicList: List<Song>? = null

    fun setMusicList(list: List<Song>) {

        if (musicList == null) {
            musicList = list
        }
    }

    fun getMusicList(): List<Song> {
        return musicList ?: emptyList()
    }
}