package com.example.duskplayer

import android.app.Activity
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

class Tools(private val activity: AppCompatActivity) {

    val STORAGE_PERMISSION_CODE = 1

    fun getPermissionFromUser() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {
            //why i need your permission dialog
            //showExplanationDialog()
            activity.finish()


        } else {
            // if user click ok i accept your request
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                STORAGE_PERMISSION_CODE
            )
        }
    }

    private fun showExplanationDialog() {
        //implement why i need your permission dialog
        AlertDialog.Builder(activity.applicationContext)
            .setTitle("permission files")
            .setMessage("for use to this app you must accept request.")
            .setPositiveButton("Ok") { _, _ ->
                //user clicked ok
                // show request permission again
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    STORAGE_PERMISSION_CODE
                )
            }
            .setNegativeButton("No Thanks") { _, _ ->
                activity.finish()
            }
            .show()
    }
}