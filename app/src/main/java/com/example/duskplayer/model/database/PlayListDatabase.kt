package com.example.duskplayer.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.duskplayer.model.entity.Playlist
import com.example.duskplayer.model.entity.Song

@Database(entities = [Playlist::class, Song::class], version = 1)

abstract class PlayListDatabase : RoomDatabase() {
    abstract fun playListDao(): PlayListDao

//    companion object {
//        @Volatile
//        private var INSTANCE: PlayListDatabase? = null
//
//        fun getDatabase(context: Context): PlayListDatabase {
//            return INSTANCE ?: synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    PlayListDatabase::class.java,
//                    "app_database"
//                ).build()
//                INSTANCE = instance
//                instance
//            }
//        }
//    }
}