package com.a.dproject.mvvm.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.a.dproject.R

class TestActivity : AppCompatActivity() {
    companion object {
        var contextLeak: Context? = null
    }
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        TestActivity.contextLeak = this
        setContentView(R.layout.activity_test)
        val v = findViewById<View>(R.id.root);
        v.setOnClickListener {
            val i = Intent();
            i.setClass(baseContext!!, Test2Activity::class.java)

            startActivity(i)

        }
        Log.e("Test","TestActivity.onCreate");
    }

    override fun onStart() {
        super.onStart()
        Log.e("Test","TestActivity.onStart");
    }

    override fun onStop() {
        super.onStop()
        Log.e("Test","TestActivity.onStop");
    }

    override fun onPause() {
        super.onPause()
        Log.e("Test","TestActivity.onPause");
    }

    override fun onResume() {
        super.onResume()
        Log.e("Test","TestActivity.onResume");
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("Test","TestActivity.onCreate");
    }


}