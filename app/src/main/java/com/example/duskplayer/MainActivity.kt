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
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //initialize classes
        dialogs = DuskDialogs()

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
            Toast.makeText(this, "Thank You (;", Toast.LENGTH_SHORT).show()

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
                if (grantResults.isEmpty() && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    //if permission not generated
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
}