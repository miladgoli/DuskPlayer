package com.example.duskplayer.view

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import com.example.duskplayer.R
import com.google.android.material.button.MaterialButton

/*
create custom dialogs
 */

class DuskDialogs {

    //This dialog is used when I want to tell the user why this app needs permission.
    fun whyNeedPermissionDialog(
        context: Context,
        yesListener: View.OnClickListener,
        noListener: View.OnClickListener
    ) {

        val dialog = Dialog(context)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.why_permission_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val yesBtn = dialog.findViewById(R.id.btnYesWhyPermissionDialog) as MaterialButton
        val noBtn = dialog.findViewById(R.id.btnNoWhyPermissionDialog) as MaterialButton

        yesBtn.setOnClickListener {
            yesListener.onClick(it)
            dialog.dismiss()
        }

        noBtn.setOnClickListener {
            noListener.onClick(it)
            dialog.dismiss()
        }

        dialog.show()
    }

    //This dialog is used when we want to guide the user to the settings to grant access.
    fun goSettingsForPermissionDialog(
        context: Context,
        settingsListener: View.OnClickListener,
        exitListener: View.OnClickListener
    ) {

        val dialog = Dialog(context)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.settings_permission_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val settingsBtn = dialog.findViewById(R.id.btnGoSettingPermissionDialog) as MaterialButton
        val exitBtn = dialog.findViewById(R.id.btnExitSettingPermissionDialog) as MaterialButton

        settingsBtn.setOnClickListener {
            settingsListener.onClick(it)
            dialog.dismiss()
        }

        exitBtn.setOnClickListener {
            exitListener.onClick(it)
            dialog.dismiss()
        }

        dialog.show()
    }
}