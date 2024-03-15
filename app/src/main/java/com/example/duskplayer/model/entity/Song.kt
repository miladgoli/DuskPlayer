package com.example.duskplayer.model.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Entity(tableName = "tbl_songs")
@Parcelize
data class Song(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var title: String,
    var duration: Long,
    var artist: String,
    var path: String,
    var albumArt: String,
    var isFavorite: Boolean,
    var playlistId: Long? // افزودن ستون playlistId به جدول Song
) : Parcelable