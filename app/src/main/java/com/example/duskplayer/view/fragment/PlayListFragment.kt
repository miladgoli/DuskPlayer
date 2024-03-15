package com.example.duskplayer.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.duskplayer.R
import com.example.duskplayer.databinding.FragmentPlayListBinding
import com.example.duskplayer.model.entity.PlaylistWithSongs
import com.example.duskplayer.model.entity.Song
import com.example.duskplayer.view.adapter.MainMusicsRecAdapter

class PlayListFragment : Fragment(), MainMusicsRecAdapter.MainMusicAdapterCallBack {

    private lateinit var binding: FragmentPlayListBinding
    private lateinit var playList: PlaylistWithSongs
    private lateinit var mainPlayListsAdapter: MainMusicsRecAdapter
    private var musicList = ArrayList<Song>()
    private var shuffleList = ArrayList<Song>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlayListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //get playlist from main fragment
        playList = arguments?.getParcelable("playlist")!!
        binding.tvPlayListName.text = playList.playlist.name
        //setup recycler view
        mainPlayListsAdapter = MainMusicsRecAdapter(this)
        binding.musicRecyclerPlayList.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.musicRecyclerPlayList.adapter = mainPlayListsAdapter
        if (playList.songs?.isNotEmpty()!!) {
            if (musicList.size == 0) {
                musicList = playList.songs as ArrayList<Song>
                mainPlayListsAdapter.setMusicsList(musicList)
            }
        }


    }

    override fun onMusicItemClicked(song: Song, position: Int) {
        val bundle = Bundle()
        bundle.putParcelableArrayList("list", musicList)
        shuffleList.addAll(musicList)
        shuffleList.shuffle()
        bundle.putParcelableArrayList("shuffle_list", shuffleList)
        bundle.putInt("position", position)
        // Navigate to the details fragment
        val fragment = PlayingMusicFragment()
        fragment.arguments = bundle
        val fragmentManager: FragmentManager = parentFragmentManager
        fragmentManager.beginTransaction()
            .replace(R.id.mainFragment, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onResume() {
        super.onResume()
        if (musicList.size == 0) {
            musicList = playList.songs as ArrayList<Song>
            mainPlayListsAdapter.setMusicsList(musicList)
        }
    }
}