package com.example.duskplayer

import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.duskplayer.databinding.FragmentMainBinding

class MainFragment : Fragment(), MainMusicsRecAdapter.MainAdapterCallBack {

    lateinit var binding: FragmentMainBinding

    private var musicList = ArrayList<Song>()

    private lateinit var mainMusicsAdapter: MainMusicsRecAdapter

    //private lateinit var mainPlayListsAdapter:
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
        loadMusics()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeRecycleViews()
        binding.tvMusicsSize.text = musicList.size.toString()

    }

    private fun initializeRecycleViews() {

        binding.musicRecyclerMain.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.musicRecyclerMain.adapter = mainMusicsAdapter
    }

    private fun loadMusics() {

        val contentResolver = requireActivity().contentResolver
        val songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val sortOrder = MediaStore.Audio.Media.TITLE + " ASC"
        val cursor = contentResolver.query(
            songUri,
            null,
            null,
            null,
            sortOrder
        )

        if (cursor != null && cursor.moveToFirst()) {
            val titleColumn = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
            val idColumn = cursor.getColumnIndex(MediaStore.Audio.Media._ID)
            val durationColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)
            val artistColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)

            do {
                val id = cursor.getLong(idColumn)
                val title = cursor.getString(titleColumn)
                val duration = cursor.getLong(durationColumn)
                val artist = cursor.getString(artistColumn)

                musicList.add(
                    Song(
                        id,
                        title,
                        duration,
                        artist,
                        false
                    )
                )
            } while (cursor.moveToNext())
        }

        cursor?.close()

        mainMusicsAdapter.setMusicsList(musicList)
    }

    //when we clicked on music
    override fun onItemClicked(song: Song) {
        val navController = findNavController()
        val bundle = Bundle()
        bundle.putParcelable("song", song)
        val animation = NavOptions.Builder()
            .setEnterAnim(R.anim.slide_left)
            .setExitAnim(R.anim.slide_right)
            .build()
        navController.navigate(
            R.id.action_mainFragment_to_playingMusicFragment,
            bundle,
            animation
        )
    }
}