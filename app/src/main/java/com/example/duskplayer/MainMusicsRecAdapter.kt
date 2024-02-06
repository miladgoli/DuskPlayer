package com.example.duskplayer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


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
        holder.onBind(musics[position])
    }

    inner class MainMusicsRecViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView = itemView.findViewById(R.id.musicNameItemMusic)
        private val tvSinger: TextView = itemView.findViewById(R.id.singerNameItemMusic)
        private val moreBtn: ImageButton = itemView.findViewById(R.id.moreMusicItemMusic)
        private val coverImg: ImageView = itemView.findViewById(R.id.coverItemMusic)
        fun onBind(song: Song) {
            tvTitle.text = song.title
            tvSinger.text = song.artist

            itemView.setOnClickListener {
                callBack.onItemClicked(song)
            }
        }
    }

    fun setMusicsList(listMusic: ArrayList<Song>) {
        musics.clear()
        musics = listMusic
        notifyDataSetChanged()
    }

    interface MainAdapterCallBack {
        fun onItemClicked(song: Song)
    }
}