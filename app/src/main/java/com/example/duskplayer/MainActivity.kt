package com.example.duskplayer

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.duskplayer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onStart() {
        super.onStart()
        //get Permission
        checkStoragePermission()
    }

    private val STORAGE_PERMISSION_REQUEST_CODE = 1
    private lateinit var dialogs: DuskDialogs
    private var musicList = ArrayList<Song>()
    private lateinit var binding: ActivityMainBinding

    private lateinit var mainMusicsAdapter: MainMusicsRecAdapter
    //private lateinit var mainPlayListsAdapter:

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //initialize classes
        dialogs = DuskDialogs()
        mainMusicsAdapter = MainMusicsRecAdapter()

        initializeRecycleViews()

        loadMusics()
    }

    private fun checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // send request permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                STORAGE_PERMISSION_REQUEST_CODE
            )
        } else {
            // permission generated

        }
    }

    private fun showWhyNeedPermissionDialog() {
        dialogs.whyNeedPermissionDialog(this,
            {
                //yes Button
                //send permission request
                checkStoragePermission()

            }, {
                //no Button
                //Exit App
                finish()
                Toast.makeText(this, "Sorry !\ni don't have permission", Toast.LENGTH_SHORT).show()

            })
    }

    private fun showSettingsDialog() {
        dialogs.goSettingsForPermissionDialog(this,
            {
                //Settings Button
                //go to user settings for get permission
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivityForResult(intent, STORAGE_PERMISSION_REQUEST_CODE)

            }, {
                //Exit Button
                finish()
            })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            STORAGE_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission generated


                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            this,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE
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

        val contentResolver = contentResolver
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
                        artist
                    )
                )
            } while (cursor.moveToNext())
        }

        cursor?.close()

        mainMusicsAdapter.setMusicsList(musicList)
        binding.tvMusicsSize.text = musicList.size.toString()
    }

    private fun initializeRecycleViews() {

        binding.musicRecyclerMain.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.musicRecyclerMain.adapter = mainMusicsAdapter
    }
}