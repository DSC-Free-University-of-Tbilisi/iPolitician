package com.example.ipolitician

import android.app.Activity
import android.content.Context
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.ipolitician.Util.dialog
import com.example.ipolitician.Util.externalMap

var backgroundColor = TypedValue()
var componentColor = TypedValue()
var textColor = TypedValue()
var buttonColor = TypedValue()

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Activity.switchMode(){
    var sharedPreferences = getPreferences(Context.MODE_PRIVATE)
    with(sharedPreferences.edit()){
        if (getMode() != R.style.LightMode_NoActionBar){
            putInt("theme", R.style.LightMode_NoActionBar)
        } else {
            putInt("theme", R.style.DarkMode_NoActionBar)
        }
        commit()
    }
    setTheme(getMode())
    recreate()
}

fun Activity.getMode() : Int{
    var sharedPreferences = getPreferences(Context.MODE_PRIVATE)
    return sharedPreferences.getInt("theme", R.style.LightMode_NoActionBar)
}