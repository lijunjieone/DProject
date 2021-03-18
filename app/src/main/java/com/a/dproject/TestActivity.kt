package com.a.dproject

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
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