package com.a.dproject

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.a.dproject.mvvm.fragment.SimpleFragment
import com.a.dproject.mvvm.viewmodel.ListViewModel.Companion.PARAM_PARENT_NAME

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple)
        if (savedInstanceState == null) {
            val fragmentName = intent.getStringExtra(FRAGMENT_NAME)
            val f = if (TextUtils.isEmpty(fragmentName)) {
                SimpleFragment.newInstance()
            } else {
                createFragmentByName(fragmentName!!)
            }
//            globalApplicationContext()
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, f)
                    .commitNow()
        }
    }

    companion object {
        const val FRAGMENT_NAME = "fragmentName"
        fun startFragment(context: Context, fragmentName: String, parentName: String = "") {
            val intent = Intent();
            intent.setClass(context, MainActivity::class.java)
            intent.putExtra(FRAGMENT_NAME, fragmentName)
            intent.putExtra(PARAM_PARENT_NAME, parentName)
            context.startActivity(intent)
        }

        fun createFragmentByName(fragmentName: String): Fragment {
            val f = Class.forName("$fragmentName").newInstance() as Fragment
            return f;
        }
    }
}