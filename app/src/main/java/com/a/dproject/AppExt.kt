package com.a.dproject

import android.widget.Toast

fun Int.toast() {
    Toast.makeText(DApp.appContext, this, Toast.LENGTH_SHORT).show()
}

fun String.toast() {
    Toast.makeText(DApp.appContext, this, Toast.LENGTH_SHORT).show()
}

fun Int.string(): String {
    return DApp.appContext.getString(this)
}

