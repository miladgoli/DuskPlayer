package com.example.duskplayer.viewmodel.playlist

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.duskplayer.model.database.PlayListDao
import com.example.duskplayer.model.database.PlayListDatabase
import com.example.duskplayer.model.repository.PlayListRepository
import com.example.duskplayer.model.repository.PlayListRepositoryImp

class PlaylistViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    private lateinit var dao: PlayListDao
    lateinit var repository: PlayListRepository
    private lateinit var database: PlayListDatabase


    fun initializeDatabase() {
        database =
            Room.databaseBuilder(context, PlayListDatabase::class.java, "db_playlist").build()
        dao = database.playListDao()
        repository = PlayListRepositoryImp(dao)
    }


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        initializeDatabase()
        return PlayListViewModel(repository) as T
    }
}
