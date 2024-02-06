package com.example.duskplayer

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Song(
    val id: Long,
    var title: String,
    var duration: Long,
    var artist: String,
    var isFavorite: Boolean
) : Parcelable