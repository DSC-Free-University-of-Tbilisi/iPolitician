package com.example.ipolitician.Util

import android.app.Activity
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.ipolitician.R
import java.security.MessageDigest


val externalMap = mutableMapOf<Fragment, AlertDialog>()

var Fragment.dialog : AlertDialog
    get() = externalMap[this] ?: createDialog()
    set(value:AlertDialog) { externalMap[this] = value }

fun Fragment.createDialog() : AlertDialog{
    val builder: AlertDialog.Builder = AlertDialog.Builder(context!!)
    val customLayout: View =
        layoutInflater.inflate(R.layout.custom_loading, null)
    val dialog = builder.setView(customLayout).create()
    dialog.setCanceledOnTouchOutside(false)
    externalMap[this] = dialog
    return dialog
}

fun Activity.showAlertDialogWithAutoDismiss(message: String) {
    val builder = AlertDialog.Builder(this)
    builder.setTitle("შეტყობინება")
        .setMessage(message)
        .setCancelable(false)
    val alertDialog = builder.create()
    alertDialog.show()
    Handler().postDelayed(Runnable {
        if (alertDialog.isShowing) {
            alertDialog.dismiss()
        }
    }, 3000) //change 5000 with a specific time you want
}

fun String.md5(): String {
    return hashString(this, "MD5")
}

fun String.sha256(): String {
    return hashString(this, "SHA-256")
}

private fun hashString(input: String, algorithm: String): String {
    return MessageDigest
        .getInstance(algorithm)
        .digest(input.toByteArray())
        .fold("", { str, it -> str + "%02x".format(it) })
}