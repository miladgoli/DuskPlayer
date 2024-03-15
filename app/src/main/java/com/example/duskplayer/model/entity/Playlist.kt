package com.example.duskplayer.model.entity

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import kotlinx.parcelize.Parcelize

@Entity(tableName = "tbl_playlist")
@Parcelize
data class Playlist(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
) : Parcelable

@Parcelize
data class PlaylistWithSongs(
    @Embedded val playlist: Playlist,
    @Relation(
        parentColumn = "id",
        entityColumn = "playlistId"
    )
    val songs: List<Song>?
) : Parcelable