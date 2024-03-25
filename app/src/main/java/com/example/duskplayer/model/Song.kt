package com.example.duskplayer.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Song(
    val id: Long,
    var title: String,
    var duration: Long,
    var artist: String,
    var path: String,
    var albumArt: String,
    var isFavorite: Boolean
) : Parcelable