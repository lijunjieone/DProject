package com.bn.jni

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.add).text = "${callJavaStaticMethod3("java instance method3",1) }"

    }

    external fun stringFromJNI(): String
    external fun stringFromJNI2(): String
    external fun add(a:Int,b:Int):Int
    external fun callJavaInstaceMethod()
    external fun callJavaStaticMethod2(from:String,index:Int):String
    external fun callJavaStaticMethod3(from:String,index:Int):String

    companion object {

        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }

}