package com.a.dproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.a.dproject.mvvm.fragment.ListFragment
import com.a.dproject.mvvm.fragment.SimpleFragment
import com.sankuai.waimai.router.annotation.RouterUri

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, SimpleFragment.newInstance())
                    .commitNow()
        }
    }
}