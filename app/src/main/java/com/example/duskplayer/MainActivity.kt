package com.example.duskplayer

import android.content.ContentUris
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.example.duskplayer.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    override fun onStart() {
        super.onStart()
        //get Permission
        checkStoragePermission()
    }

    private val STORAGE_PERMISSION_REQUEST_CODE = 1
    private lateinit var dialogs: DuskDialogs
    private lateinit var binding: ActivityMainBinding
    private var musicList = ArrayList<Song>()
    private lateinit var viewModel: MusicListViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //initialize classes
        dialogs = DuskDialogs()
        viewModel = ViewModelProvider(this).get(MusicListViewModel::class.java)
    }

    //This dialog is used when I want to tell the user why this app needs permission.
    private fun showWhyNeedPermissionDialog() {
        dialogs.whyNeedPermissionDialog(this,
            {
                //pressed Yes Button
                //send permission request
                checkStoragePermission()

            }, {
                //pressed No Button
                //Exit App
                finish()
                Toast.makeText(this, "Sorry !\ni don't have permission", Toast.LENGTH_SHORT).show()

            })
    }

    //This dialog is used when we want to guide the user to the settings to grant access.
    private fun showSettingsDialog() {
        dialogs.goSettingsForPermissionDialog(this,
            {
                //pressed Settings Button
                //go to settings for get permission
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivityForResult(intent, STORAGE_PERMISSION_REQUEST_CODE)

            }, {
                //pressed Exit Button
                finish()
            })
    }

    //get permission
    private fun checkStoragePermission() {

        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_MEDIA_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // send request permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.READ_MEDIA_AUDIO),
                    STORAGE_PERMISSION_REQUEST_CODE
                )
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    STORAGE_PERMISSION_REQUEST_CODE
                )
            }
        } else {
            // permission generated
            //load music and go to fragments
            loadMusics()
            val fragment = MainFragment()
            val fragmentManager: FragmentManager = supportFragmentManager
            fragmentManager.beginTransaction()
                .replace(R.id.mainFragment, fragment)
                .commit()
        }
    }

    //check permission runtime
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            STORAGE_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission generated
                    if (viewModel.getMusicList().isNotEmpty()) {
                        //check need update list or no
                    } else {
                        //first running app and get music
                        //load music and go to fragments
                        loadMusics()
                        val fragment: MainFragment = MainFragment()
                        val fragmentManager: FragmentManager = supportFragmentManager
                        fragmentManager.beginTransaction()
                            .replace(R.id.mainFragment, fragment)
                            .commit()
                    }
                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            this, android.Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                    ) {
                        // show why i need to get permission dialog
                        showWhyNeedPermissionDialog()
                    } else {
                        // show settings dialog
                        showSettingsDialog()
                    }
                }
                return
            }
        }
    }

    private fun loadMusics() {

        musicList = ArrayList()
        val contentResolver = this.contentResolver
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
            val pathColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
            val titleColumn = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
            val idColumn = cursor.getColumnIndex(MediaStore.Audio.Media._ID)
            val durationColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)
            val artistColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)
            val albumIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)

            //Here's a loop to read and add all the songs to a music arraylist.
            while (cursor.moveToNext()) {
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
                if (path.lowercase().endsWith(".mp3") ||
                    path.lowercase().endsWith(".mp4") ||
                    path.lowercase().endsWith(".mpeg")
                ) {
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
                }
            }
        }

        cursor?.close()
        if (musicList.size > 0) {
            viewModel.setMusicList(musicList)
        }

    }
}