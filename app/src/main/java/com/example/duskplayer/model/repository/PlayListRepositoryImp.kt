package com.example.duskplayer.model.repository

import com.example.duskplayer.model.database.PlayListDao
import com.example.duskplayer.model.entity.Playlist
import com.example.duskplayer.model.entity.PlaylistWithSongs
import com.example.duskplayer.model.entity.Song
import io.reactivex.Completable
import io.reactivex.Single

class PlayListRepositoryImp(val dao: PlayListDao) : PlayListRepository {
    override fun addPlayList(playlist: Playlist): Completable {
        return dao.addPlayList(playlist)
    }

    override fun deletePlayList(playlist: Playlist): Completable {
        return dao.deletePlayList(playlist)
    }

    override fun getAllPlayLists(): Single<List<PlaylistWithSongs>> {
        return dao.getAllPlaylists()
    }

    override fun insertSong(song: Song): Completable {
        return dao.insertSong(song)
    }

    override fun removeSong(song: Song): Completable {
        return dao.deleteSong(song)
    }
}