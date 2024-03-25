package com.example.duskplayer.view

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.duskplayer.R
import com.example.duskplayer.model.Song

/*
this is main recycler view adapter and view holder
this adapter show to user all musics
 */
class MainMusicsRecAdapter(private val callBack: MainAdapterCallBack) :
    RecyclerView.Adapter<MainMusicsRecAdapter.MainMusicsRecViewHolder>() {

    private var musics = ArrayList<Song>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainMusicsRecViewHolder {
        return MainMusicsRecViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_music, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return musics.size
    }

    override fun onBindViewHolder(holder: MainMusicsRecViewHolder, position: Int) {
        holder.onBind(musics[position], position)
    }

    inner class MainMusicsRecViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //item_music.xml items for set music details
        private val tvTitle: TextView = itemView.findViewById(R.id.musicNameItemMusic)
        private val tvSinger: TextView = itemView.findViewById(R.id.singerNameItemMusic)
        private val moreBtn: ImageButton = itemView.findViewById(R.id.moreMusicItemMusic)
        private val coverImg: ImageView = itemView.findViewById(R.id.coverItemMusic)
        fun onBind(song: Song, position: Int) {
            tvTitle.text = song.title
            tvSinger.text = song.artist

            val albumArt = song.albumArt
            coverImg.setImageURI(Uri.parse(albumArt))

            if (coverImg.drawable == null) {
                coverImg.setImageResource(R.drawable.logo)
            }

            itemView.setOnClickListener {
                callBack.onItemClicked(song, position)
            }
        }
    }

    //for get musics and set in adapter and show to recyclerview.
    fun setMusicsList(listMusic: List<Song>) {
        musics.clear()
        musics = listMusic as ArrayList<Song>
        notifyDataSetChanged()
    }

    //interface for notifies the main fragment.
    interface MainAdapterCallBack {
        fun onItemClicked(song: Song, position: Int)
    }
}