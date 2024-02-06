package com.example.duskplayer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.duskplayer.databinding.FragmentPlayingMusicBinding
import java.util.concurrent.TimeUnit

class PlayingMusicFragment : Fragment() {

    private lateinit var binding: FragmentPlayingMusicBinding
    private lateinit var song: Song
    private var repeatMode: Int = Tools.REPEAT_NOT_MUSIC
    private var isPlaying: Boolean = true
    private var shuffleMode: Boolean = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlayingMusicBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupMusicOnFragment()

        onButtonsClicked()
    }

    private fun setupMusicOnFragment() {
        song = arguments?.getParcelable("song")!!
        binding.namePlayingMusic.text = song.title
        binding.singerNamePlayingMusic.text = song.artist
        binding.endTimePlayingMusic.text = formatDuration(song.duration)

        if (song.isFavorite) {
            binding.favoriteBtnPlayingMusic.setImageResource(R.drawable.ic_favorite_on_24dp)
        } else {
            binding.favoriteBtnPlayingMusic.setImageResource(R.drawable.ic_favorite_24dp)
        }
    }

    private fun formatDuration(duration: Long): String {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(duration)
        val seconds =
            TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(minutes)
        return String.format("%02d:%02d", minutes, seconds)
    }

    private fun onButtonsClicked() {

        binding.backBtnPlayingMusic.setOnClickListener {
            findNavController().navigate(R.id.action_playingMusicFragment_to_mainFragment)
        }

        binding.favoriteBtnPlayingMusic.setOnClickListener {
            if (song.isFavorite) {
                binding.favoriteBtnPlayingMusic.setImageResource(R.drawable.ic_favorite_24dp)
                song.isFavorite = false
            } else {
                binding.favoriteBtnPlayingMusic.setImageResource(R.drawable.ic_favorite_on_24dp)
                song.isFavorite = true
            }
        }

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

        binding.playBtnPlayingMusic.setOnClickListener {
            if (isPlaying) {
                binding.playBtnPlayingMusic.setImageResource(R.drawable.ic_play_24dp)
                isPlaying = false
            } else {
                binding.playBtnPlayingMusic.setImageResource(R.drawable.ic_pause_24dp)
                isPlaying = true
            }
        }

        binding.shuffleBtnPlayingMusic.setOnClickListener {
            if (shuffleMode) {
                binding.shuffleBtnPlayingMusic.setImageResource(R.drawable.ic_not_shuffle_24dp)
                shuffleMode = false
            } else {
                binding.shuffleBtnPlayingMusic.setImageResource(R.drawable.ic_shuffle_24dp)
                shuffleMode = true
            }
        }
    }
}