package com.a.dproject

import android.app.Application


class DApp : Application() {


    override fun onCreate() {
        super.onCreate()
        appContext = this
    }

    companion object {

        lateinit var appContext: Application
    }

}