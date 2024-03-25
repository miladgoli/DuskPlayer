package com.example.duskplayer.viewmodel

import androidx.lifecycle.ViewModel
import com.example.duskplayer.model.Song

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