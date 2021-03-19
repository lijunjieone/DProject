package com.a.dproject.mvvm.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.a.dproject.R

class Test2Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        val v = findViewById<View>(R.id.root);

        v.setOnClickListener {
            val i = Intent();
            i.setClass(baseContext!!, TestActivity::class.java)

            startActivity(i)

        }
        Log.e("Test","onCreate");
    }

    override fun onStart() {
        super.onStart()
        Log.e("Test","onStart");
    }

    override fun onStop() {
        super.onStop()
        Log.e("Test","onStop");
    }

    override fun onPause() {
        super.onPause()
        Log.e("Test","onPause");
    }

    override fun onResume() {
        super.onResume()
        Log.e("Test","onResume");
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("Test","onCreate");
    }


}