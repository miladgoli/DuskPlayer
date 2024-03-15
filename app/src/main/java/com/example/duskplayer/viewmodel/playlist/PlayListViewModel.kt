package com.example.duskplayer.viewmodel.playlist

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.duskplayer.model.database.PlayListDao
import com.example.duskplayer.model.database.PlayListDatabase
import com.example.duskplayer.model.entity.Playlist
import com.example.duskplayer.model.entity.PlaylistWithSongs
import com.example.duskplayer.model.entity.Song
import com.example.duskplayer.model.repository.PlayListRepository
import io.reactivex.CompletableObserver
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class PlayListViewModel(val repository: PlayListRepository) : ViewModel() {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    //MutableLiveData
    private val errorsMu: MutableLiveData<String> = MutableLiveData<String>()
    private val addPlayListMu: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    private val getPlayListsMu: MutableLiveData<List<PlaylistWithSongs>> =
        MutableLiveData<List<PlaylistWithSongs>>()
    private val deletePlayListMu: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    private val insertSongMu: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    private val removeSongMu: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

     fun addPlayList(playlist: Playlist) {
        repository.addPlayList(playlist)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onComplete() {
                    addPlayListMu.postValue(true)
                }

                override fun onError(e: Throwable) {
                    errorsMu.postValue(e.toString())
                }

            })
    }

    private fun getAllPlayLists() {
        repository.getAllPlayLists()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<List<PlaylistWithSongs>> {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onError(e: Throwable) {
                    errorsMu.postValue(e.toString())
                }

                override fun onSuccess(t: List<PlaylistWithSongs>) {
                    getPlayListsMu.postValue(t)
                }

            })
    }

    fun getPlayLists(): LiveData<List<PlaylistWithSongs>> {
        getAllPlayLists()
        return getPlayListsMu
    }
}