package com.a.dproject

import android.widget.Toast
import androidx.fragment.app.FragmentManager

fun Int.toast() {
    Toast.makeText(DApp.appContext, this, Toast.LENGTH_SHORT).show()
}

fun String.toast() {
    try {
        Toast.makeText(DApp.appContext, this, Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        //don't print
    }
}

fun Int.string(): String {
    return DApp.appContext.getString(this)
}


fun Int.showFragment(
    fragment: androidx.fragment.app.Fragment,
    childFragmentManager: FragmentManager
) {
    val fragmentManager = childFragmentManager
    fragmentManager.beginTransaction().replace(this, fragment, fragment.javaClass.simpleName)
        .commitNow()
}