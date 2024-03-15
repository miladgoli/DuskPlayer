package com.example.duskplayer.viewmodel

import androidx.lifecycle.ViewModel
import com.example.duskplayer.model.entity.PlaylistWithSongs
import com.example.duskplayer.model.entity.Song

class MusicListViewModel() : ViewModel() {

    private var musicList: List<Song>? = null
    private var playLists: List<PlaylistWithSongs>? = null

    fun setMusicList(list: List<Song>) {

        if (musicList == null) {
            musicList = list
        }
    }

    fun setPlayList(list: List<PlaylistWithSongs>) {

        if (playLists == null) {
            playLists = list
        }
    }

    fun getMusicList(): List<Song> {
        return musicList ?: emptyList()
    }

    fun getPlayLists(): List<PlaylistWithSongs> {
        return playLists ?: emptyList()
    }
}