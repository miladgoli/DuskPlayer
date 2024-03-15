package com.example.duskplayer.model.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.duskplayer.model.entity.Playlist
import com.example.duskplayer.model.entity.PlaylistWithSongs
import com.example.duskplayer.model.entity.Song
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface PlayListDao {
    @Insert
     fun addPlayList(playList: Playlist): Completable

    @Delete
     fun deletePlayList(playList: Playlist): Completable

    @Query("SELECT * FROM tbl_playlist")
    fun getAllPlaylists(): Single<List<PlaylistWithSongs>>

    @Insert
     fun insertSong(song: Song):Completable

    @Delete
     fun deleteSong(song: Song) : Completable

}