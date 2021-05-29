package com.example.ipolitician.Util

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.ipolitician.R
import com.example.ipolitician.hideKeyboard

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