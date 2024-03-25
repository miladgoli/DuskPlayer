package com.example.duskplayer.view.fragment

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import com.example.duskplayer.R
import com.example.duskplayer.databinding.FragmentPlayingMusicBinding
import com.example.duskplayer.model.Song
import com.example.duskplayer.model.Tools
import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.TimeUnit

class PlayingMusicFragment : Fragment() {

    private lateinit var binding: FragmentPlayingMusicBinding
    private lateinit var song: Song
    private lateinit var songsList: ArrayList<Song>
    private lateinit var originalList: ArrayList<Song>
    private lateinit var shuffleList: ArrayList<Song>
    private var positionNowPlaying: Int = -1
    private var repeatMode: Int = Tools.REPEAT_NOT_MUSIC
    private var isPlaying: Boolean = true
    private var shuffleMode: Boolean = false
    private var mediaPlayer = MediaPlayer()
    private lateinit var timer: Timer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlayingMusicBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //get music info from main fragment and ready music for play.
        setupMusic()
        //set page details and views
        setupMusicUiFragment()
        //buttons reaction
        onButtonsClicked()

    }

    private fun setupMusic() {
        //set variables
        positionNowPlaying = arguments?.getInt("position")!!
        shuffleList = (arguments?.getParcelableArrayList("shuffle_list")!!)
        originalList = (arguments?.getParcelableArrayList("list"))!!
        songsList=ArrayList()
        songsList.addAll(originalList)
        song = songsList.get(positionNowPlaying)
        //play music
        mediaPlayer.reset()
        mediaPlayer.setDataSource(song.path)
        mediaPlayer.prepare()
        mediaPlayer.start()
        isPlaying = true
        //update page details
        checkFinishedMusic()
    }

    private fun setupMusicUiFragment() {
        //set timer for music time and seekbar
        if (::timer.isInitialized) {
            timer.cancel()
        }
        timer = Timer()
        //check shuffle mode
        if (shuffleMode) {
            songsList = ArrayList()
            songsList.addAll(shuffleList)
        } else {
            songsList = ArrayList()
            songsList.addAll(originalList)
        }
        //set play button icon
        if (!isPlaying) {
            binding.playBtnPlayingMusic.setImageResource(R.drawable.ic_play_24dp)
        } else {
            binding.playBtnPlayingMusic.setImageResource(R.drawable.ic_pause_24dp)
        }
        //set music image
        val albumArt = song.albumArt
        binding.coverPlayingMusic.setImageURI(Uri.parse(albumArt))
        //if music hasn't cover
        if (binding.coverPlayingMusic.drawable == null) {
            binding.coverPlayingMusic.setImageResource(R.drawable.logo)
        }
        //initialize seekBar
        binding.seekBarPlayingMusic.max = mediaPlayer.duration
        //change seekbar position from user
        binding.seekBarPlayingMusic.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.startTimePlayingMusic.text =
                    formatDuration(progress.toLong())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if (seekBar != null) {
                    //go to custom position and update views
                    mediaPlayer.seekTo(seekBar.progress)
                }

            }
        })

        //timer for music time and update views
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                if (mediaPlayer.isPlaying) {
                    requireActivity().runOnUiThread {

                        binding.seekBarPlayingMusic.progress = mediaPlayer.currentPosition
                        binding.startTimePlayingMusic.text =
                            formatDuration(mediaPlayer.currentPosition.toLong())
                    }
                }
            }
        }, 0, 1000)
        //set music details view
        binding.namePlayingMusic.text = song.title
        binding.singerNamePlayingMusic.text = song.artist
        binding.endTimePlayingMusic.text = formatDuration(song.duration)
        //check music favorite
        if (song.isFavorite) {
            binding.favoriteBtnPlayingMusic.setImageResource(R.drawable.ic_favorite_on_24dp)
        } else {
            binding.favoriteBtnPlayingMusic.setImageResource(R.drawable.ic_favorite_24dp)
        }
    }

    //convert time into readable format time text
    private fun formatDuration(duration: Long): String {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(duration)
        val seconds =
            TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(minutes)
        return String.format("%02d:%02d", minutes, seconds)
    }

    private fun onButtonsClicked() {
        //btn back to main page
        binding.backBtnPlayingMusic.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        //btn is favorite
        binding.favoriteBtnPlayingMusic.setOnClickListener {
            if (song.isFavorite) {
                binding.favoriteBtnPlayingMusic.setImageResource(R.drawable.ic_favorite_24dp)
                song.isFavorite = false
            } else {
                binding.favoriteBtnPlayingMusic.setImageResource(R.drawable.ic_favorite_on_24dp)
                song.isFavorite = true
            }
        }
        //btn next music
        binding.nextBtnPlayingMusic.setOnClickListener {
            nextMusic()
        }
        //btn last music
        binding.previousBtnPlayingMusic.setOnClickListener {
            previousMusic()
        }
        //btn repeat mode
        binding.repeatBtnPlayingMusic.setOnClickListener {
            if (repeatMode == Tools.REPEAT_NOT_MUSIC) {
                repeatMode++
                binding.repeatBtnPlayingMusic.setImageResource(R.drawable.ic_repeat_one_24dp)
            } else if (repeatMode == Tools.REPEAT_ONE_MUSIC) {
                repeatMode++
                binding.repeatBtnPlayingMusic.setImageResource(R.drawable.ic_repeat_24dp)
            } else {
                repeatMode = Tools.REPEAT_NOT_MUSIC
                binding.repeatBtnPlayingMusic.setImageResource(R.drawable.ic_not_repeat_24dp)
            }
        }
        //btn pause and play music
        binding.playBtnPlayingMusic.setOnClickListener {
            if (isPlaying) {
                binding.playBtnPlayingMusic.setImageResource(R.drawable.ic_play_24dp)
                mediaPlayer.pause()
                isPlaying = false
            } else {
                binding.playBtnPlayingMusic.setImageResource(R.drawable.ic_pause_24dp)
                mediaPlayer.start()
                isPlaying = true
            }
        }
        //btn shuffle mode
        binding.shuffleBtnPlayingMusic.setOnClickListener {
            if (shuffleMode) {
                binding.shuffleBtnPlayingMusic.setImageResource(R.drawable.ic_not_shuffle_24dp)
                shuffleMode = false
                //update position playing music in original list.
                positionNowPlaying = originalList.indexOf(songsList[positionNowPlaying])
                songsList = ArrayList()
                songsList.addAll(originalList)
            } else {
                binding.shuffleBtnPlayingMusic.setImageResource(R.drawable.ic_shuffle_24dp)
                shuffleMode = true
                songsList = ArrayList()
                songsList.addAll(shuffleList)
            }
        }
    }

    //destroy mediaplayer and cancel timer when fragment closed
    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
        mediaPlayer.stop()
        mediaPlayer.release()
    }

    //if finished music worked this function
    private fun checkFinishedMusic() {
        mediaPlayer.setOnCompletionListener {
            if (repeatMode == Tools.REPEAT_NOT_MUSIC) {
                //stop music and not repeat any musics and update views.
                mediaPlayer.pause()
                mediaPlayer.seekTo(0)
                binding.seekBarPlayingMusic.progress = 0
                binding.startTimePlayingMusic.text =
                    formatDuration(mediaPlayer.currentPosition.toLong())
                isPlaying = false
                setupMusicUiFragment()
            } else if (repeatMode == Tools.REPEAT_ONE_MUSIC) {
                //play music again and update views.
                mediaPlayer.start()
                isPlaying = true
                setupMusicUiFragment()
            } else {
                //play next music and update views.
                nextMusic()
            }
        }
    }

    //when clicked next music button
    private fun nextMusic() {
        positionNowPlaying++
        //check end music of list
        if (positionNowPlaying == songsList.size) {
            positionNowPlaying = 0
        }
        //update now playing music.
        mediaPlayer.reset()
        mediaPlayer.setDataSource(songsList.get(positionNowPlaying).path)
        song = songsList.get(positionNowPlaying)
        mediaPlayer.prepare()
        mediaPlayer.start()
        isPlaying = true
        setupMusicUiFragment()
    }

    //when clicked previous music button
    private fun previousMusic() {
        positionNowPlaying--
        if (positionNowPlaying < 0) {
            positionNowPlaying = songsList.size - 1
        }
        mediaPlayer.reset()
        mediaPlayer.setDataSource(songsList.get(positionNowPlaying).path)
        song = songsList.get(positionNowPlaying)
        mediaPlayer.prepare()
        mediaPlayer.start()
        isPlaying = true
        setupMusicUiFragment()
    }
}