package com.example.duskplayer.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.duskplayer.R
import com.example.duskplayer.databinding.FragmentMainBinding
import com.example.duskplayer.model.entity.Playlist
import com.example.duskplayer.model.entity.PlaylistWithSongs
import com.example.duskplayer.model.entity.Song
import com.example.duskplayer.view.adapter.MainMusicsRecAdapter
import com.example.duskplayer.view.adapter.MainPlayListsAdapter
import com.example.duskplayer.viewmodel.MusicListViewModel
import com.example.duskplayer.viewmodel.playlist.PlayListViewModel
import com.example.duskplayer.viewmodel.playlist.PlaylistViewModelFactory


class MainMusicFragment : Fragment(), MainMusicsRecAdapter.MainMusicAdapterCallBack,
    MainPlayListsAdapter.MainPlayListAdapterCallBack {

    lateinit var binding: FragmentMainBinding


    private var musicList = ArrayList<Song>()
    private var shuffleList = ArrayList<Song>()
    private var playLists = ArrayList<PlaylistWithSongs>()
    private lateinit var mainMusicsAdapter: MainMusicsRecAdapter
    private lateinit var mainPlayListsAdapter: MainPlayListsAdapter
    private lateinit var musicsViewModel: MusicListViewModel
    private lateinit var playListViewModel: PlayListViewModel
//    private val playListViewModel: PlayListViewModel by viewModels {
//        PlaylistViewModelFactory(requireActivity().application)
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //initialize classes
        mainMusicsAdapter = MainMusicsRecAdapter(this)
        mainPlayListsAdapter = MainPlayListsAdapter(this)
        musicsViewModel = ViewModelProvider(requireActivity()).get(MusicListViewModel::class.java)
        playListViewModel = ViewModelProvider(
            this,
            PlaylistViewModelFactory(requireContext())
        ).get(PlayListViewModel::class.java)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeRecycleViews()

        //show user musics size
        binding.tvMusicsSize.text = musicList.size.toString()

    }

    private fun initializeRecycleViews() {
        //main recycler view for show user musics
        binding.musicRecyclerMain.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.musicRecyclerMain.adapter = mainMusicsAdapter
        //main recycler view for show user play lists
        binding.playlistRecyclerMain.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        binding.playlistRecyclerMain.adapter = mainPlayListsAdapter

        if (playLists.size == 0) {
            playListViewModel.getPlayLists().observe(requireActivity(), Observer {
                if (it.isEmpty()) {
                    val favoritePlayList = Playlist(1, "Favorite")
                    playListViewModel.addPlayList(favoritePlayList)
                    playLists.add(PlaylistWithSongs(favoritePlayList, null))
                    musicsViewModel.setPlayList(playLists)
                    mainPlayListsAdapter.setPlayLists(playLists)
                } else {
                    playLists = it as ArrayList<PlaylistWithSongs>
                    musicsViewModel.setPlayList(playLists)
                    mainPlayListsAdapter.setPlayLists(playLists)

                }
            })
        }

        //get and set musics from viewmodel
        if (musicList.size == 0) {
            musicList = musicsViewModel.getMusicList() as ArrayList<Song>
            mainMusicsAdapter.setMusicsList(musicList)
        }
    }

    //when user clicked on music (impl main adapter interface).
    override fun onMusicItemClicked(song: Song, position: Int) {
        //go to playing fragment for play clicked music.
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

    override fun onPlayListItemClicked(playList: PlaylistWithSongs) {
        //go to play list fragment
        val bundle = Bundle()
        bundle.putParcelable("playlist", playList)
        // Navigate to the play list fragment
        val fragment = PlayListFragment()
        fragment.arguments = bundle
        val fragmentManager: FragmentManager = parentFragmentManager
        fragmentManager.beginTransaction()
            .replace(R.id.mainFragment, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onResume() {
        super.onResume()
        //get and set musics from viewmodel
        if (musicList.size == 0) {
            musicList = musicsViewModel.getMusicList() as ArrayList<Song>
            mainMusicsAdapter.setMusicsList(musicList)
        }

        if (playLists.size == 0) {
            playListViewModel.getPlayLists().observe(requireActivity(), Observer {
                if (it.isNotEmpty()) {
                    playLists = musicsViewModel.getPlayLists() as ArrayList<PlaylistWithSongs>
                    mainPlayListsAdapter.setPlayLists(playLists)
                }
            })
        }
    }

}