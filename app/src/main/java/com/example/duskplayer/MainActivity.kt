package com.example.duskplayer

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private val STORAGE_PERMISSION_REQUEST_CODE = 1
    private lateinit var dialogs: DuskDialogs
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dialogs = DuskDialogs()

    }

    override fun onStart() {
        super.onStart()

        //get Permission
        checkStoragePermission()
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


    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivityForResult(intent, STORAGE_PERMISSION_REQUEST_CODE)
    }
}