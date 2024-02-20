package com.example.duskplayer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.duskplayer.databinding.FragmentMainBinding


class MainFragment : Fragment(), MainMusicsRecAdapter.MainAdapterCallBack {

    lateinit var binding: FragmentMainBinding
    private var musicList = ArrayList<Song>()
    private var shuffleList = ArrayList<Song>()
    private lateinit var mainMusicsAdapter: MainMusicsRecAdapter
    private lateinit var viewModel: MusicListViewModel
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
        viewModel = ViewModelProvider(requireActivity()).get(MusicListViewModel::class.java)
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
        //get and set musics from viewmodel
        if (musicList.size==0){
            musicList = viewModel.getMusicList() as ArrayList<Song>
            mainMusicsAdapter.setMusicsList(musicList)
        }
    }

    //when user clicked on music (impl main adapter interface).
    override fun onItemClicked(song: Song, position: Int) {
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

    override fun onResume() {
        super.onResume()
        //get and set musics from viewmodel
        if (musicList.size==0){
            musicList = viewModel.getMusicList() as ArrayList<Song>
            mainMusicsAdapter.setMusicsList(musicList)
        }
    }
}