package com.example.duskplayer.model.repository

import androidx.lifecycle.LiveData
import com.example.duskplayer.model.entity.Playlist
import com.example.duskplayer.model.entity.PlaylistWithSongs
import com.example.duskplayer.model.entity.Song
import io.reactivex.Completable
import io.reactivex.Single

interface PlayListRepository {
    fun addPlayList(playlist: Playlist): Completable
    fun deletePlayList(playlist: Playlist): Completable
    fun getAllPlayLists(): Single<List<PlaylistWithSongs>>
    fun insertSong(song: Song): Completable
    fun removeSong(song: Song): Completable
}