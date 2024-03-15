package com.example.duskplayer.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.duskplayer.R
import com.example.duskplayer.model.entity.PlaylistWithSongs

class MainPlayListsAdapter(private val callBack: MainPlayListAdapterCallBack) :
    RecyclerView.Adapter<MainPlayListsAdapter.MainPlayListsViewHolder>() {

    private var playLists = ArrayList<PlaylistWithSongs>()

    inner class MainPlayListsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var name = itemView.findViewById<TextView>(R.id.namePlaylistItemTxt)
        fun onBind(playList: PlaylistWithSongs) {
            name.text = playList.playlist.name

            itemView.setOnClickListener {
                callBack.onPlayListItemClicked(playList)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainPlayListsViewHolder {
        return MainPlayListsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_playlist, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return playLists.size
    }

    override fun onBindViewHolder(holder: MainPlayListsViewHolder, position: Int) {
        holder.onBind(playLists[position])
    }

    fun addPlayList(playList: PlaylistWithSongs) {
        playLists.add(playList)
        notifyItemInserted(0)
    }

    fun setPlayLists(list: ArrayList<PlaylistWithSongs>) {
        playLists = ArrayList()
        playLists.addAll(list)
        notifyDataSetChanged()
    }

    fun deletePlayList(playList: PlaylistWithSongs) {
        playLists.remove(playList)
        notifyDataSetChanged()
    }

    interface MainPlayListAdapterCallBack {
        fun onPlayListItemClicked(playList: PlaylistWithSongs)
    }
}