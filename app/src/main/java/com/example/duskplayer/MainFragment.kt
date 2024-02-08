package com.example.duskplayer

import android.content.ContentUris
import android.net.Uri
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
    private var shuffleList = ArrayList<Song>()
    private lateinit var mainMusicsAdapter: MainMusicsRecAdapter

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

        //show user musics size
        binding.tvMusicsSize.text = musicList.size.toString()

    }

    private fun initializeRecycleViews() {
        //main recycler view for show user musics
        binding.musicRecyclerMain.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.musicRecyclerMain.adapter = mainMusicsAdapter
    }

    //get all music from user phone
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
            val pathColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
            val albumIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)

            //Here's a loop to read and add all the songs to a music arraylist.
            do {
                val id = cursor.getLong(idColumn)
                val title = cursor.getString(titleColumn)
                val duration = cursor.getLong(durationColumn)
                val artist = cursor.getString(artistColumn)
                val path = cursor.getString(pathColumn)
                val albumId = cursor.getLong(albumIdColumn)
                val albumArtUri = ContentUris.withAppendedId(
                    Uri.parse("content://media/external/audio/albumart"),
                    albumId
                )

                musicList.add(
                    Song(
                        id,
                        title,
                        duration,
                        artist,
                        path,
                        albumArtUri.toString(),
                        false
                    )
                )
            } while (cursor.moveToNext())
        }

        cursor?.close()

        //set main recycler view music list adapter.
        mainMusicsAdapter.setMusicsList(musicList)
    }

    //when user clicked on music (impl main adapter interface).
    override fun onItemClicked(song: Song, position: Int) {
        //go to playing fragment for play clicked music with animation.
        val navController = findNavController()
        val bundle = Bundle()
        bundle.putSerializable("list", musicList)
        shuffleList.addAll(musicList)
        shuffleList.shuffle()
        bundle.putSerializable("shuffle_list", shuffleList)
        bundle.putInt("position", position)
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