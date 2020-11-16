package com.a.dproject.mvvm.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.a.dproject.R
import com.a.dproject.mvvm.fragment.ListFragment
import com.hunter.library.debug.HunterDebugClass

@HunterDebugClass
class SimpleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ListFragment.newInstance())
                .commitNow()
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStart() {
        super.onStart()
    }
}
