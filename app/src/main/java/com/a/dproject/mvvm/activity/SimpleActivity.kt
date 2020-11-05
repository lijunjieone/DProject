package com.a.dproject.mvvm.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.a.dproject.R
import com.a.dproject.mvvm.fragment.ListFragment

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
}
